package com.creswave.blog.service.login.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.creswave.blog.domain.user.User;
import com.creswave.blog.helper.hash.HashHelper;
import com.creswave.blog.helper.jwt.JwtHelper;
import com.creswave.blog.helper.string.StringHelper;
import com.creswave.blog.repository.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private Logger logger = LoggerFactory.getLogger( LoginService.class );

    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
    private final AuthenticationManager authenticationManager;
    private final StringHelper stringHelper;
    private final HashHelper hashHelper;

    @Value( "${first-name.validation.minimum-length}" )
    private Integer lengthMinimumFirstName;

    @Value( "${last-name.validation.minimum-length}" )
    private Integer lengthMinimumLastName;

    @Value( "${username.validation.minimum-length}" )
    private Integer lengthMinimumUsername;



    @Transactional
    public LoginResponse authenticate( LoginRequest request ) {

        String email = stringHelper.trimAndLowerCase( request.getEmail() );
        String password = stringHelper.trim( request.getPassword() );
        User user = userRepository.findByEmail( email ).orElseThrow();
        
        System.out.println( "user " + user.toString() );

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken( email, password ) );

        String jwtToken = jwtHelper.generateToken( email );
        jwtHelper.revokeAllUserTokens( user );
        jwtHelper.saveUserToken( user, jwtToken );

        return LoginResponse.builder()
                .token( jwtToken )
                .build();
    }

}
