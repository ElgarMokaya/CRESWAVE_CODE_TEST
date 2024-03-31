package com.creswave.blog.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.creswave.blog.helper.jwt.JwtHelper;
import com.creswave.blog.repository.token.TokenRepository;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger( JwtAuthenticationFilter.class );

    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain ) throws ServletException, IOException {

        final String authHeader = request.getHeader( "Authorization" );
        final String jwt;
        final String userEmail;

        if ( authHeader == null || !authHeader.startsWith( "Bearer " ) ) {
            filterChain.doFilter( request, response );
            return;
        }

        // here we have a bearer 
        jwt = authHeader.substring( 7 );

        try {
            userEmail = jwtHelper.extractUsername( jwt );

            if ( userEmail != null && SecurityContextHolder.getContext()
                    .getAuthentication() == null ) {

                UserDetails userDetails = this.userDetailsService.loadUserByUsername( userEmail );

                var isTokenValid = tokenRepository.findByToken( jwt )
                        .map( t -> !t.isExpired() && !t.isRevoked() )
                        .orElse( false );

                if ( jwtHelper.isTokenValid( jwt, userDetails ) && isTokenValid ) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities() );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails( request ) );

                    SecurityContextHolder.getContext().setAuthentication( authToken );
                }
            }
        }
        catch ( ExpiredJwtException e ) {

            request.setAttribute( "exception", e );
            response.setHeader( "X-Error-Type", "JWT_EXPIRED" );
            response.sendError( HttpStatus.UNAUTHORIZED.value(), "JWT expired" );
            throw e;
        }

        filterChain.doFilter( request, response );
    }
}
