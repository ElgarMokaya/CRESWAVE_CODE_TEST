package com.creswave.blog.post.comment.update;

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

import com.creswave.blog.service.blog.post.comment.UpdatePostCommentRequest;
import com.creswave.blog.service.blog.post.comment.UpdatePostCommentResponse;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT )
@ActiveProfiles( "integrationtest" )
public class UpdatePostCommentIT {

    private Logger logger = LoggerFactory.getLogger( UpdatePostCommentIT.class );

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int randomServerPort;

    private Long updatePostComment( Long postId, Long commentId, String token )
            throws URISyntaxException {

        final String baseUrl =
                "http://localhost:" + randomServerPort + "/api/v1/blog/posts/" + postId
                        + "/comments/" + commentId;

        URI uri = new URI( baseUrl );

        String commentText = 
                "I can help my github username is: elgarmokaya. Raise a ticket on " +
                        "one of my projects to get in touch with me";

        UpdatePostCommentRequest updatePostCommentRequest =
                UpdatePostCommentRequest.builder()
                        .text( commentText ).build();

        HttpHeaders headers = new HttpHeaders();
        headers.set( "Authorization", "Bearer " + token );

        HttpEntity<UpdatePostCommentRequest> request =
                new HttpEntity<>( updatePostCommentRequest, headers );

        ResponseEntity<UpdatePostCommentResponse> result = this.restTemplate.exchange(
                uri, HttpMethod.PUT, request, UpdatePostCommentResponse.class );

        int statusCode = result.getStatusCode().value();
        logger.info( "statusCode: " + statusCode );
        Assert.assertEquals( 201, statusCode );

        UpdatePostCommentResponse response = result.getBody();
        logger.info( response.toString() );

        Assert.assertFalse( response.getHasValdationErrors() );
        Assert.assertNotNull( response.getId() );
        Assert.assertEquals( commentText, response.getText() );
        return response.getId();
    }

    /**
     * I am not testing all execution paths in this integration test.  I have instead re-factored 
     * code out into Validator classes which build a response object and return a HttpStatus code.
     * The Validator classes can be easily tested with Unit Tests. This means I can unit test the 
     * different execution paths which is faster from a coding and execution perspective than 
     * coding all execution paths in the integration test.
     */
    @Test
    @Sql( scripts = "/sql/com/creswave/blog/postcomment/update/updatePostComment.sql",
            config = @SqlConfig( transactionMode = TransactionMode.ISOLATED ),
            executionPhase = ExecutionPhase.BEFORE_TEST_METHOD )
    @Transactional
    @Rollback
    @DirtiesContext( methodMode = MethodMode.AFTER_METHOD )
    void standardUserCreatePostTest() throws URISyntaxException {

        // expires in 29 years time at time of writing. See https://token.dev/
        String standardUserToken =
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGdhci5ib3NpYm9yaS5tb2theWFAZ21haWwuY29tIiwiaWF0IjoxNzExODAzNzUxLCJleHAiOjI2MTE4MDM3NTF9.6GHmSrGMUxcoTlgdyySuW--PO0bLU9H1CRzX0Z4RCaY";

        Long postId = 1l;
        Long commentId = 1l;
        Long postCommentId = updatePostComment( postId, commentId, standardUserToken );
        logger.info( "postCommentId: " + postCommentId );
        logger.info( "Completed CreatePostCommentIT" );
    }
}
