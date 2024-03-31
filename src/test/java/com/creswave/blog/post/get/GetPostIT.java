package com.creswave.blog.post.get;

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

import com.creswave.blog.service.blog.post.CommentDTO;
import com.creswave.blog.service.blog.post.GetPostResponse;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT )
@ActiveProfiles( "integrationtest" )
public class GetPostIT {

    private Logger logger = LoggerFactory.getLogger( GetPostIT.class );

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int randomServerPort;


    private void getPostFound( String token, Long postId ) throws URISyntaxException {

        final String baseUrl =
                "http://localhost:" + randomServerPort + "/api/v1/blog/posts/" + postId;

        HttpHeaders headers = new HttpHeaders();
        headers.set( "Authorization", "Bearer " + token );

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<GetPostResponse> result =
                this.restTemplate.exchange(
                        baseUrl,
                        HttpMethod.GET,
                        request,
                        GetPostResponse.class );

        int statusCode = result.getStatusCode().value();
        logger.info( "statusCode: " + statusCode );
        Assert.assertEquals( 200, statusCode );
        GetPostResponse response = result.getBody();
        logger.info(response.toString());

        Assert.assertNotNull(response.getId());
        Assert.assertNotNull(response.getTitle());
        Assert.assertNotNull(response.getText());

        CommentDTO comment = response.getComments().get( 0 );
        Assert.assertEquals( 10000l, comment.getId().longValue() );
        Assert.assertEquals( "I can help my github username is: elgarmokaya", comment.getText() );
        comment.getText();
    }


    private void getPostNotFound( String token, Long postId ) throws URISyntaxException {

        final String baseUrl =
                "http://localhost:" + randomServerPort + "/api/v1/blog/posts/" + postId;

        HttpHeaders headers = new HttpHeaders();
        headers.set( "Authorization", "Bearer " + token );

        HttpEntity<Void> request = new HttpEntity<>( headers );

        ResponseEntity<GetPostResponse> result =
                this.restTemplate.exchange( baseUrl, HttpMethod.GET, request,
                        GetPostResponse.class );

        int statusCode = result.getStatusCode().value();
        logger.info( "statusCode: " + statusCode );
        Assert.assertEquals( 404, statusCode );
        GetPostResponse response = result.getBody();

        Assert.assertNull( response );
    }


    @Test
    @Sql( scripts = "/sql/com/creswave/blog/post/get/getPost.sql",
            config = @SqlConfig( transactionMode = TransactionMode.ISOLATED ),
            executionPhase = ExecutionPhase.BEFORE_TEST_METHOD )
    @Transactional
    @Rollback
    @DirtiesContext( methodMode = MethodMode.AFTER_METHOD )
    void standardUserGetPostTest() throws URISyntaxException {

        // expires in 29 years time at time of writing. See https://token.dev/
        String standardUserToken =
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGdhci5ib3NpYm9yaS5tb2theWFAZ21haWwuY29tIiwiaWF0IjoxNzExODAzNzUxLCJleHAiOjI2MTE4MDM3NTF9.6GHmSrGMUxcoTlgdyySuW--PO0bLU9H1CRzX0Z4RCaY";

        Long postId = 1l;

        getPostFound( standardUserToken, postId );
        logger.info( "Completed GetPostT" );
    }


    @Test
    @Sql( scripts = "/sql/com/creswave/blog/post/get/getPost.sql",
            config = @SqlConfig( transactionMode = TransactionMode.ISOLATED ),
            executionPhase = ExecutionPhase.BEFORE_TEST_METHOD )
    @Transactional
    @Rollback
    @DirtiesContext( methodMode = MethodMode.AFTER_METHOD )
    void standardUserGetPostNotFoundTest() throws URISyntaxException {

        // expires in 29 years time at time of writing. See https://token.dev/
        String standardUserToken =
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGdhci5ib3NpYm9yaS5tb2theWFAZ21haWwuY29tIiwiaWF0IjoxNzExODAzNzUxLCJleHAiOjI2MTE4MDM3NTF9.6GHmSrGMUxcoTlgdyySuW--PO0bLU9H1CRzX0Z4RCaY";

        Long postId = 10l;

        getPostNotFound( standardUserToken, postId );
        logger.info( "Completed GetPostT" );
    }
}
