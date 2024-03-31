package com.creswave.blog.post.delete;

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
import org.springframework.http.HttpMethod;
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

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT )
@ActiveProfiles( "integrationtest" )
public class DeletePostIT {

    private Logger logger = LoggerFactory.getLogger( DeletePostIT.class );

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int randomServerPort;

    private void standardUserDeleteFails( Long postId, String token ) throws URISyntaxException {

        final String baseUrl =
                "http://localhost:" + randomServerPort + "/api/v1/blog/posts/" + postId;

        URI url = new URI( baseUrl );
        HttpHeaders headers = new HttpHeaders();
        headers.set( "Authorization", "Bearer " + token );

        HttpEntity<Void> request = new HttpEntity<>( headers );

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.DELETE,
                request,
                Void.class );

        int statusCode = response.getStatusCode().value();
        logger.info( "statusCode: " + statusCode );
        Assert.assertEquals( 403, statusCode );
    }


    private void admninDeletePasses( Long postId, String token ) throws URISyntaxException {

        final String baseUrl =
                "http://localhost:" + randomServerPort + "/api/v1/blog/posts/" + postId;

        URI url = new URI( baseUrl );
        HttpHeaders headers = new HttpHeaders();
        headers.set( "Authorization", "Bearer " + token );

        HttpEntity<Void> request = new HttpEntity<>( headers );

        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                request,
                Void.class );

        int statusCode = response.getStatusCode().value();
        logger.info( "statusCode: " + statusCode );
        Assert.assertEquals( 204, statusCode );
    }


    @Test
    @Sql( scripts = "/sql/com/creswave/blog/post/delete/deletePost.sql",
            config = @SqlConfig( transactionMode = TransactionMode.ISOLATED ),
            executionPhase = ExecutionPhase.BEFORE_TEST_METHOD )
    @Transactional
    @Rollback
    @DirtiesContext( methodMode = MethodMode.AFTER_METHOD )
    void standardUserDeleteFails() throws URISyntaxException {

        // expires in 29 years time at time of writing. See https://token.dev/
        String standardUserToken =
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGdhci5ib3NpYm9yaS5tb2theWFAZ21haWwuY29tIiwiaWF0IjoxNzExODAzNzUxLCJleHAiOjI2MTE4MDM3NTF9.6GHmSrGMUxcoTlgdyySuW--PO0bLU9H1CRzX0Z4RCaY";

        Long postId = 1l;

        standardUserDeleteFails( postId, standardUserToken );
    }


    @Test
    @Sql( scripts = "/sql/com/creswave/blog/post/delete/deletePost.sql",
            config = @SqlConfig( transactionMode = TransactionMode.ISOLATED ),
            executionPhase = ExecutionPhase.BEFORE_TEST_METHOD )
    @Transactional
    @Rollback
    @DirtiesContext( methodMode = MethodMode.AFTER_METHOD )
    void admninDeletePasses() throws URISyntaxException {

        // expires in 29 years time at time of writing. See https://token.dev/
        String adminToken =
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBibG9nLmNvbSIsImlhdCI6MTcxMTg0NTIyNiwiZXhwIjoyNjExODQ1MjI2fQ.R3usP8H-iqNano5aUegCepTGZFZ40GcgcT55Zfzk4-k";

        Long postId = 1l;

        admninDeletePasses( postId, adminToken );
    }
}
