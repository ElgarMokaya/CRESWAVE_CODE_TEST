package ut.com.creswave.blog.service.blog.post;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import com.creswave.blog.domain.blog.Post;
import com.creswave.blog.service.blog.post.UpdatePostResponse;
import com.creswave.blog.service.blog.post.UpdatePostResponse.UpdatePostResponseBuilder;
import com.creswave.blog.service.blog.post.UpdatePostValidator;
import com.creswave.blog.service.blog.post.UpdatePostValidatorImpl;


/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public class UpdatePostValidatorTest {

    private static UpdatePostValidator updatePostValidator;

    @Test
    public void updatePostValid() {

        updatePostValidator = new UpdatePostValidatorImpl();
        ReflectionTestUtils.setField( updatePostValidator, "postTitleMinimumLength", 3 );
        ReflectionTestUtils.setField( updatePostValidator, "postTitleMaximumLength", 256 );
        ReflectionTestUtils.setField( updatePostValidator, "postTextMinimumLength", 3 );
        ReflectionTestUtils.setField( updatePostValidator, "postTextMaximumLength", 4000 );
        
        String title = "Java Spring Boot";
        String text = "I want to learn Java Spring Boot.  Can anyone help?";

        UpdatePostResponseBuilder builder = UpdatePostResponse.builder();
        Post post = Post.builder().id( 10000l ).title( title ).text( text ).build();

        HttpStatus status = updatePostValidator.validate( post, builder, title, text );
        Assert.assertEquals( 200, status.value() );

        UpdatePostResponse response = builder.build();
        Assert.assertFalse( response.getHasValdationErrors() );
        Assert.assertFalse( response.getTitleToShort() );
        Assert.assertFalse( response.getTitleToLong() );
        Assert.assertFalse( response.getTextToShort() );
        Assert.assertFalse( response.getTextToLong() );
        Assert.assertNotNull( response.getPostTitleMinimumLength() );
        Assert.assertNotNull( response.getPostTitleMaximumLength() );
        Assert.assertNotNull( response.getPostTextMinimumLength() );
        Assert.assertNotNull( response.getPostTextMaximumLength() );
    }


    @Test
    public void postNotFound() {

        updatePostValidator = new UpdatePostValidatorImpl();
        ReflectionTestUtils.setField( updatePostValidator, "postTitleMinimumLength", 3 );
        ReflectionTestUtils.setField( updatePostValidator, "postTitleMaximumLength", 256 );
        ReflectionTestUtils.setField( updatePostValidator, "postTextMinimumLength", 3 );
        ReflectionTestUtils.setField( updatePostValidator, "postTextMaximumLength", 4000 );

        String title = "Java Spring Boot";
        String text = "I want to learn Java Spring Boot.  Can anyone help?";

        UpdatePostResponseBuilder builder = UpdatePostResponse.builder();
        Post post = null;

        HttpStatus status = updatePostValidator.validate( post, builder, title, text );
        Assert.assertEquals( 404, status.value() );
    }


    @Test
    public void titleTooShort() {

        updatePostValidator = new UpdatePostValidatorImpl();
        ReflectionTestUtils.setField( updatePostValidator, "postTitleMinimumLength", 3 );
        ReflectionTestUtils.setField( updatePostValidator, "postTitleMaximumLength", 256 );
        ReflectionTestUtils.setField( updatePostValidator, "postTextMinimumLength", 3 );
        ReflectionTestUtils.setField( updatePostValidator, "postTextMaximumLength", 4000 );

        String title = "J";
        String text = "I want to learn Java Spring Boot.  Can anyone help?";

        UpdatePostResponseBuilder builder = UpdatePostResponse.builder();
        Post post = Post.builder().id( 10000l ).title( title ).text( text ).build();

        HttpStatus status = updatePostValidator.validate( post, builder, title, text );
        Assert.assertEquals( 422, status.value() );

        UpdatePostResponse response = builder.build();
        Assert.assertTrue( response.getHasValdationErrors() );
        Assert.assertTrue( response.getTitleToShort() );
        Assert.assertFalse( response.getTitleToLong() );
        Assert.assertFalse( response.getTextToShort() );
        Assert.assertFalse( response.getTextToLong() );
        Assert.assertNotNull( response.getPostTitleMinimumLength() );
        Assert.assertNotNull( response.getPostTitleMaximumLength() );
        Assert.assertNotNull( response.getPostTextMinimumLength() );
        Assert.assertNotNull( response.getPostTextMaximumLength() );
    }


    @Test
    public void titleTooLong() {

        updatePostValidator = new UpdatePostValidatorImpl();
        ReflectionTestUtils.setField( updatePostValidator, "postTitleMinimumLength", 3 );
        ReflectionTestUtils.setField( updatePostValidator, "postTitleMaximumLength", 5 );
        ReflectionTestUtils.setField( updatePostValidator, "postTextMinimumLength", 3 );
        ReflectionTestUtils.setField( updatePostValidator, "postTextMaximumLength", 4000 );

        String title = "Java Spring Boot";
        String text = "I want to learn Java Spring Boot.  Can anyone help?";

        UpdatePostResponseBuilder builder = UpdatePostResponse.builder();
        Post post = Post.builder().id( 10000l ).title( title ).text( text ).build();

        HttpStatus status = updatePostValidator.validate( post, builder, title, text );
        Assert.assertEquals( 422, status.value() );

        UpdatePostResponse response = builder.build();
        Assert.assertTrue( response.getHasValdationErrors() );
        Assert.assertFalse( response.getTitleToShort() );
        Assert.assertTrue( response.getTitleToLong() );
        Assert.assertFalse( response.getTextToShort() );
        Assert.assertFalse( response.getTextToLong() );
        Assert.assertNotNull( response.getPostTitleMinimumLength() );
        Assert.assertNotNull( response.getPostTitleMaximumLength() );
        Assert.assertNotNull( response.getPostTextMinimumLength() );
        Assert.assertNotNull( response.getPostTextMaximumLength() );
    }


    @Test
    public void textTooShort() {

        updatePostValidator = new UpdatePostValidatorImpl();
        ReflectionTestUtils.setField( updatePostValidator, "postTitleMinimumLength", 3 );
        ReflectionTestUtils.setField( updatePostValidator, "postTitleMaximumLength", 256 );
        ReflectionTestUtils.setField( updatePostValidator, "postTextMinimumLength", 3 );
        ReflectionTestUtils.setField( updatePostValidator, "postTextMaximumLength", 4000 );

        String title = "Java Spring Boot";
        String text = "I";

        UpdatePostResponseBuilder builder = UpdatePostResponse.builder();
        Post post = Post.builder().id( 10000l ).title( title ).text( text ).build();

        HttpStatus status = updatePostValidator.validate( post, builder, title, text );
        Assert.assertEquals( 422, status.value() );

        UpdatePostResponse response = builder.build();
        Assert.assertTrue( response.getHasValdationErrors() );
        Assert.assertTrue( response.getTitleToShort() );
        Assert.assertFalse( response.getTitleToLong() );
        Assert.assertFalse( response.getTextToShort() );
        Assert.assertFalse( response.getTextToLong() );
        Assert.assertNotNull( response.getPostTitleMinimumLength() );
        Assert.assertNotNull( response.getPostTitleMaximumLength() );
        Assert.assertNotNull( response.getPostTextMinimumLength() );
        Assert.assertNotNull( response.getPostTextMaximumLength() );
    }


    @Test
    public void textTooLong() {

        updatePostValidator = new UpdatePostValidatorImpl();
        ReflectionTestUtils.setField( updatePostValidator, "postTitleMinimumLength", 3 );
        ReflectionTestUtils.setField( updatePostValidator, "postTitleMaximumLength", 7 );
        ReflectionTestUtils.setField( updatePostValidator, "postTextMinimumLength", 3 );
        ReflectionTestUtils.setField( updatePostValidator, "postTextMaximumLength", 4000 );

        String title = "Java Spring Boot";
        String text = "I want to learn Java Spring Boot.  Can anyone help?";

        UpdatePostResponseBuilder builder = UpdatePostResponse.builder();
        Post post = Post.builder().id( 10000l ).title( title ).text( text ).build();

        HttpStatus status = updatePostValidator.validate( post, builder, title, text );
        Assert.assertEquals( 422, status.value() );

        UpdatePostResponse response = builder.build();
        Assert.assertTrue( response.getHasValdationErrors() );
        Assert.assertFalse( response.getTitleToShort() );
        Assert.assertTrue( response.getTitleToLong() );
        Assert.assertFalse( response.getTextToShort() );
        Assert.assertFalse( response.getTextToLong() );
        Assert.assertNotNull( response.getPostTitleMinimumLength() );
        Assert.assertNotNull( response.getPostTitleMaximumLength() );
        Assert.assertNotNull( response.getPostTextMinimumLength() );
        Assert.assertNotNull( response.getPostTextMaximumLength() );
    }
}
