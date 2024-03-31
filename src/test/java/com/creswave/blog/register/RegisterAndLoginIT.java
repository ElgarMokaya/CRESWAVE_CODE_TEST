package com.creswave.blog.register;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlConfig.TransactionMode;
import org.springframework.transaction.annotation.Transactional;

import com.creswave.blog.service.account.register.email.RegisterRequest;
import com.creswave.blog.service.account.register.email.RegisterResponse;
import com.creswave.blog.service.login.email.LoginRequest;
import com.creswave.blog.service.login.email.LoginResponse;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT )
@ActiveProfiles( "integrationtest" )
public class RegisterAndLoginIT {

    private Logger logger = LoggerFactory.getLogger( RegisterAndLoginIT.class );

    private static final String EMAIL_EM = "elgar.bosibori.mokaya@gmail.com";
    private static final String PASSWORD_EM = "a123AB$#aaaaa";
    private static final String USERNAME_EM = "elation";

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int randomServerPort;

    private String standardUserlogin() throws URISyntaxException {

        final String baseUrl =
                "http://localhost:" + randomServerPort + "/api/v1/login/authenticate";

        URI uri = new URI( baseUrl );

        LoginRequest loginRequest =
                LoginRequest.builder().email( EMAIL_EM ).password( PASSWORD_EM ).build();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<LoginRequest> request = new HttpEntity<>( loginRequest, headers );

        ResponseEntity<LoginResponse> result =
                this.restTemplate.postForEntity( uri, request, LoginResponse.class );

        LoginResponse response = result.getBody();
        logger.info( "response: " + response );

        int statusCode = result.getStatusCode().value();
        logger.info( "statusCode: " + statusCode );
        Assert.assertEquals( 200, statusCode );

        Assert.assertNotNull( response );
        String token = response.getToken();
        Assert.assertNotNull( token );
        logger.info( "token: " + token );
        return token;
    }


    private void standardUserRegister() throws URISyntaxException {

        final String baseUrl =
                "http://localhost:" + randomServerPort + "/api/v1/register/save";

        URI uri = new URI( baseUrl );

        RegisterRequest registerRequest =
                RegisterRequest.builder()
                        .countryId( 1l )
                        .email( EMAIL_EM )
                        .password( PASSWORD_EM )
                        .passwordAlt( PASSWORD_EM )
                        .username( USERNAME_EM )
                        .build();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<RegisterRequest> request = new HttpEntity<>( registerRequest, headers );

        ResponseEntity<RegisterResponse> result =
                this.restTemplate.postForEntity( uri, request, RegisterResponse.class );

        int statusCode = result.getStatusCode().value();
        logger.info( "statusCode: " + statusCode );
        Assert.assertEquals( 201, statusCode );
        RegisterResponse response = result.getBody();
        Assert.assertNotNull( response );
        Assert.assertTrue( response.getSuccess() );
        logger.info( "response: " + response );
    }


    @Test
    @Sql( scripts = "/sql/com/creswave/blog/register/register.sql",
            config = @SqlConfig( transactionMode = TransactionMode.ISOLATED ),
            executionPhase = ExecutionPhase.BEFORE_TEST_METHOD )
    @Transactional
    @Rollback
    @DirtiesContext( methodMode = MethodMode.AFTER_METHOD )
    void standardUserRegisterAndLoginTest() throws URISyntaxException {

        // TestTransaction.flagForRollback();

        // Register as a standard user.
        standardUserRegister();

        // Login as a standard user and retrieve the token.
        String standardUserToken = standardUserlogin();
        logger.info( "standardUserToken: " + standardUserToken );
        logger.info( "Completed RegisterAndLoginIT" );
    }
}
