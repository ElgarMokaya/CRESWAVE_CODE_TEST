package com.creswave.blog.service.logout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.creswave.blog.repository.token.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private Logger logger = LoggerFactory.getLogger( LogoutService.class );

    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication ) {

        logger.info( "Logging out" );

        final String authHeader = request.getHeader( "Authorization" );
        final String jwt;

        if ( authHeader == null || !authHeader.startsWith( "Bearer " ) ) {

            return;
        }

        jwt = authHeader.substring( 7 );
        var storedToken = tokenRepository.findByToken( jwt ).orElse( null );

        if ( storedToken != null ) {

            storedToken.setExpired( true );
            storedToken.setRevoked( true );
            tokenRepository.save( storedToken );
            SecurityContextHolder.clearContext();
        }
    }
}
