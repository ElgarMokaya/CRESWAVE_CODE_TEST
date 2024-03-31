package com.creswave.blog.helper.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.creswave.blog.domain.token.Token;
import com.creswave.blog.domain.token.TokenType;
import com.creswave.blog.domain.user.User;
import com.creswave.blog.repository.token.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Component
public class JwtHelperImpl implements JwtHelper {

    private Logger logger = LoggerFactory.getLogger( JwtHelperImpl.class );

    @Value( "${jwt.life-in-milliseconds}" )
    private Long lifeInMilliSeconds;

    private static final String SECRET_KEY =
            "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @Autowired
    private TokenRepository tokenRepository;

    private boolean isTokenExpired( String token ) {

        return extractExpiration( token ).before( new Date() );
    }


    private Date extractExpiration( String token ) {

        return extractClaim( token, Claims::getExpiration );
    }


    private Claims extractAllClaims( String token ) {

        return Jwts
                .parserBuilder()
                .setSigningKey( getSignInKey() )
                .build()
                .parseClaimsJws( token )
                .getBody();
    }


    private Key getSignInKey() {

        byte[] keyBytes = Decoders.BASE64.decode( SECRET_KEY );
        return Keys.hmacShaKeyFor( keyBytes );
    }


    public JwtHelperImpl() {

    }


    @Override
    public String extractUsername( String token ) {

        return extractClaim( token, Claims::getSubject );
    }


    @Override
    public <T> T extractClaim( String token, Function<Claims, T> claimsResolver ) {

        final Claims claims = extractAllClaims( token );
        return claimsResolver.apply( claims );
    }


    @Override
    public String generateToken( String username ) {

        return generateToken( new HashMap<>(), username );
    }


    @Override
    public String generateToken(
            Map<String, Object> extraClaims,
            String userName ) {

        logger.info( "lifeInMilliSeconds: " + lifeInMilliSeconds );

        return Jwts
                .builder()
                .setClaims( extraClaims )
                .setSubject( userName )
                .setIssuedAt( new Date( System.currentTimeMillis() ) )
                .setExpiration( new Date( System.currentTimeMillis() + lifeInMilliSeconds ) )
                .signWith( getSignInKey(), SignatureAlgorithm.HS256 )
                .compact();
    }


    @Override
    public boolean isTokenValid( String token, UserDetails userDetails ) {

        final String username = extractUsername( token );
        return ( username.equals( userDetails.getUsername() ) ) && !isTokenExpired( token );
    }


    @Override
    public void revokeAllUserTokens( User user ) {

        var validUserTokens = tokenRepository.findAllValidTokenByUser( user.getId() );

        if ( validUserTokens.isEmpty() )
            return;

        validUserTokens.forEach( token -> {
            token.setExpired( true );
            token.setRevoked( true );
        } );

        tokenRepository.saveAll( validUserTokens );
    }


    @Override
    public void saveUserToken( User user, String jwtToken ) {

        var token = Token.builder()
                .user( user )
                .token( jwtToken )
                .tokenType( TokenType.BEARER )
                .expired( false )
                .revoked( false )
                .build();

        tokenRepository.save( token );
    }


    public static void main( String[] args ) {

        JwtHelperImpl jwtHelper = new JwtHelperImpl();
        jwtHelper.lifeInMilliSeconds = 900000000000l;
        System.out.println( jwtHelper.generateToken( "admin@blog.com" ) );
    }
}
