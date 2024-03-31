package com.creswave.blog.helper.jwt;

import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import com.creswave.blog.domain.user.User;

import io.jsonwebtoken.Claims;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public interface JwtHelper {

    String extractUsername( String token );


    <T> T extractClaim( String token, Function<Claims, T> claimsResolver );


    String generateToken( String username );


    String generateToken(
            Map<String, Object> extraClaims,
            String userName );


    boolean isTokenValid( String token, UserDetails userDetails );


    void revokeAllUserTokens( User user );


    void saveUserToken( User user, String jwtToken );

}