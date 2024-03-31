package com.creswave.blog.post.comment.delete;

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

import com.creswave.blog.post.delete.DeletePostIT;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT )
@ActiveProfiles( "integrationtest" )
public class DeletePostCommentIT {

    private Logger logger = LoggerFactory.getLogger( DeletePostIT.class );

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int randomServerPort;

    private void standardUserDeletePostComment( Long postId, Long commentId, String token )
            throws URISyntaxException {

        final String baseUrl =
                "http://localhost:" + randomServerPort + "/api/v1/blog/posts/" + postId +
                        "/comments/" + commentId;

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
        Assert.assertEquals( 204, statusCode );
    }



    @Test
    @Sql( scripts = "/sql/com/creswave/blog/postcomment/delete/deletePostComment.sql",
            config = @SqlConfig( transactionMode = TransactionMode.ISOLATED ),
            executionPhase = ExecutionPhase.BEFORE_TEST_METHOD )
    @Transactional
    @Rollback
    @DirtiesContext( methodMode = MethodMode.AFTER_METHOD )
    void standardUserDeletePostComment() throws URISyntaxException {

        // expires in 29 years time at time of writing. See https://token.dev/
        String standardUserToken =
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGdhci5ib3NpYm9yaS5tb2theWFAZ21haWwuY29tIiwiaWF0IjoxNzExODAzNzUxLCJleHAiOjI2MTE4MDM3NTF9.6GHmSrGMUxcoTlgdyySuW--PO0bLU9H1CRzX0Z4RCaY";

        Long postId = 1l;
        Long commentId = 1l;

        standardUserDeletePostComment( postId, commentId, standardUserToken );
    }
}
