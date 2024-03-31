package com.creswave.blog.register;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlConfig.TransactionMode;

import com.creswave.blog.service.account.register.email.RegisterRequest;
import com.creswave.blog.service.account.register.email.RegisterResponse;
import com.creswave.blog.service.blog.post.CreatePostRequest;
import com.creswave.blog.service.blog.post.CreatePostResponse;
import com.creswave.blog.service.blog.post.UpdatePostRequest;
import com.creswave.blog.service.blog.post.UpdatePostResponse;
import com.creswave.blog.service.login.email.LoginRequest;
import com.creswave.blog.service.login.email.LoginResponse;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
// @SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT )
@ActiveProfiles( "integrationtest" )
public class RegisterAndLogin_backup {

    private Logger logger = LoggerFactory.getLogger( RegisterAndLogin_backup.class );

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


    private Long createPost( String token ) throws URISyntaxException {

        final String baseUrl =
                "http://localhost:" + randomServerPort + "/api/v1/blog/posts";

        URI uri = new URI( baseUrl );

        String title = "Java Spring Boot";
        String text = "I want to learn Java Spring Boot.  Can anyone help?";
        
        CreatePostRequest createPostRequest =
                CreatePostRequest.builder()
                        .title( title )
                        .text( text ).build();

        HttpHeaders headers = new HttpHeaders();
        headers.set( "Authorization", "Bearer " + token );

        HttpEntity<CreatePostRequest> request = new HttpEntity<>( createPostRequest, headers );

        ResponseEntity<CreatePostResponse> result =
                this.restTemplate.postForEntity( uri, request, CreatePostResponse.class );

        int statusCode = result.getStatusCode().value();
        logger.info( "statusCode: " + statusCode );
        Assert.assertEquals( 201, statusCode );
        CreatePostResponse response = result.getBody();
        logger.info( response.toString() );
        Assert.assertFalse( response.getHasValdationErrors() );
        Assert.assertNotNull( response.getId() );
        Assert.assertEquals( title, response.getTitle() );
        Assert.assertEquals( text, response.getText() );
        return response.getId();
    }


    private void updatePost( String token, Long postId ) throws URISyntaxException {

        final String baseUrl =
                "http://localhost:" + randomServerPort + "/api/v1/blog/posts/" + postId;

        URI uri = new URI( baseUrl );

        String title = "Java Spring Boot is the best";
        String text = "I want to learn Java Spring Boot so I rock.  Can anyone help?";

        UpdatePostRequest updatePostRequest =
                UpdatePostRequest.builder()
                        .title( title )
                        .text( text ).build();

        HttpHeaders headers = new HttpHeaders();
        headers.set( "Authorization", "Bearer " + token );

        HttpEntity<UpdatePostRequest> request = new HttpEntity<>( updatePostRequest, headers );

        ResponseEntity<UpdatePostResponse> result = this.restTemplate.exchange(
                uri, HttpMethod.PUT, request, UpdatePostResponse.class );

        int statusCode = result.getStatusCode().value();
        logger.info( "statusCode: " + statusCode );
        Assert.assertEquals( 200, statusCode );
        UpdatePostResponse response = result.getBody();
        logger.info( "ZZZZZ " + response.toString() );
        Assert.assertFalse( response.getHasValdationErrors() );
        Assert.assertNotNull( response.getId() );
        Assert.assertEquals( title, response.getTitle() );
        Assert.assertEquals( text, response.getText() );
    }


    private Long addCommentToPost( String token, Long postId ) {

        return null;
    }


    private void getPost( String token, Long postId ) {

        //        final String baseUrl =
        //                "http://localhost:" + randomServerPort + "/api/v1/blog/posts/" + postId;
        //
        //        // These are from the above updatePost
        //        String title = "Java Spring Boot is the best";
        //        String text = "I want to learn Java Spring Boot so I rock.  Can anyone help?";
        //
        //        HttpHeaders headers = new HttpHeaders();
        //        headers.set( "Authorization", "Bearer " + token );
        //
        //        HttpEntity<Void> request = new HttpEntity<>(headers);
        //
        //        ResponseEntity<GetPostResponse> result =
        //               this.restTemplate.exchange(baseUrl, HttpMethod.GET, request, GetPostResponse.class);
        //
        //        int statusCode = result.getStatusCode().value();
        //        logger.info("statusCode: " + statusCode);
        //        Assert.assertEquals( 200, statusCode );
        //        GetPostResponse response = result.getBody();
        //        logger.info(response.toString());
        //
        //        Assert.assertNotNull(response.getId());
        //        Assert.assertEquals( title, response.getTitle() );
        //        Assert.assertEquals( text, response.getText() );
    }


    private Long updateComment( String token, Long postId, Long commentId ) {

        return null;
    }


    private void deleteComment( String token, Long postId, Long commentId ) {

    }


    private void failDeletingPost( String token, Long postId ) {

    }


    private String adminLogin() {

        return null;
    }


    private void deletePost( String token, Long postId ) {

    }


    private void create10Posts( String standardUserToken, String[][] data ) {

    }


    private void getAllPosts() {

    }


    /**
     * I am not testing all execution paths in this integration test.  I have instead re-factored 
     * code out into Validator classes which build a response object and return a HttpStatus code.
     * The Validator classes can be easily tested with Unit Tests. This means I can unit test the 
     * different execution paths which is faster from a coding and execution perspective than 
     * coding all execution paths in the integration test.
     */
    // @Test
    @Sql( scripts = "/sql/com/creswave/blog/register/register.sql",
            config = @SqlConfig( transactionMode = TransactionMode.ISOLATED ),
            executionPhase = ExecutionPhase.BEFORE_TEST_METHOD )
    void standardUserRegisterAndLoginTest() throws URISyntaxException {

        // Register as a standard user.
        standardUserRegister();

        // Login as a standard user and retreive the token.
        String standardUserToken = standardUserlogin();
        logger.info( "Completed RegisterAndLoginIT" );
    }

    /**
        // Create a post using the standard user token.
        Long postId = createPost( standardUserToken );
    
        // Update the post using standard user token.
        updatePost( standardUserToken, postId );
    
        // Add a comment to the post using the standard user token.
        Long commentId = addCommentToPost( standardUserToken, postId );
    
        // Get the post using the standard user token.
        getPost( standardUserToken, postId );
    
        // Update the comment using the standard user token.
        updateComment( standardUserToken, postId, commentId );
    
        // Delete the comment using the standard user token.
        deleteComment( standardUserToken, postId, commentId );
    
        // Add a comment back to the post using the standard user token.  I want to later test
        // deleting a post which has a comment.
        commentId = addCommentToPost( standardUserToken, postId );
    
        // Try deleting the post using the standard user token. This should fail as the standard
        // user does not have the permissions to delete a post.  Only the admin has.
        failDeletingPost( standardUserToken, postId );
    
        // The Admin does not need to register. I added the admin to standing data.  Here we login
        // and retrieve the admin token.
        String adminToken = adminLogin();
    
        // Delete the post using the admin token.
        deletePost( adminToken, postId );
    
        create10Posts( standardUserToken,
                new String[][] {
                        new String[] {
                                "A bat is great!",
                                "A bat makes for good soup" },
                        new String[] {
                                "A bat uses sonar! ",
                                "Viruses can move from bats to humans" },
                        new String[] { "", "" },
                        new String[] { "", "" },
                        new String[] { "", "" },
                        new String[] { "", "" },
                        new String[] { "", "" },
                        new String[] { "", "" },
                        new String[] { "", "" },
                        new String[] { "", "" },
    
                } );
    
    
    }
    **/
}
