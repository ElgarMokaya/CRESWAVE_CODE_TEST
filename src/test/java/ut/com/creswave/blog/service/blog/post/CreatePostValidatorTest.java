package ut.com.creswave.blog.service.blog.post;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import com.creswave.blog.service.blog.post.CreatePostResponse;
import com.creswave.blog.service.blog.post.CreatePostValidator;
import com.creswave.blog.service.blog.post.CreatePostValidatorImpl;
import com.creswave.blog.service.blog.post.CreatePostResponse.CreatePostResponseBuilder;


/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public class CreatePostValidatorTest {

    private static CreatePostValidator createPostValidator;

    @Test
    public void createPostValid() {

        createPostValidator = new CreatePostValidatorImpl();
        ReflectionTestUtils.setField( createPostValidator, "postTitleMinimumLength", 3 );
        ReflectionTestUtils.setField( createPostValidator, "postTitleMaximumLength", 256 );
        ReflectionTestUtils.setField( createPostValidator, "postTextMinimumLength", 3 );
        ReflectionTestUtils.setField( createPostValidator, "postTextMaximumLength", 4000 );
        
        String title = "Java Spring Boot";
        String text = "I want to learn Java Spring Boot.  Can anyone help?";

        CreatePostResponseBuilder builder = CreatePostResponse.builder();
        HttpStatus status = createPostValidator.validate( builder, title, text );
        Assert.assertEquals( 201, status.value() );

        CreatePostResponse response = builder.build();
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
    public void titleTooShort() {

        createPostValidator = new CreatePostValidatorImpl();
        ReflectionTestUtils.setField( createPostValidator, "postTitleMinimumLength", 3 );
        ReflectionTestUtils.setField( createPostValidator, "postTitleMaximumLength", 256 );
        ReflectionTestUtils.setField( createPostValidator, "postTextMinimumLength", 3 );
        ReflectionTestUtils.setField( createPostValidator, "postTextMaximumLength", 4000 );

        String title = "J";
        String text = "I want to learn Java Spring Boot.  Can anyone help?";

        CreatePostResponseBuilder builder = CreatePostResponse.builder();
        HttpStatus status = createPostValidator.validate( builder, title, text );
        Assert.assertEquals( 422, status.value() );

        CreatePostResponse response = builder.build();
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

        createPostValidator = new CreatePostValidatorImpl();
        ReflectionTestUtils.setField( createPostValidator, "postTitleMinimumLength", 3 );
        ReflectionTestUtils.setField( createPostValidator, "postTitleMaximumLength", 5 );
        ReflectionTestUtils.setField( createPostValidator, "postTextMinimumLength", 3 );
        ReflectionTestUtils.setField( createPostValidator, "postTextMaximumLength", 4000 );

        String title = "Java Spring Boot";
        String text = "I want to learn Java Spring Boot.  Can anyone help?";

        CreatePostResponseBuilder builder = CreatePostResponse.builder();
        HttpStatus status = createPostValidator.validate( builder, title, text );
        Assert.assertEquals( 422, status.value() );

        CreatePostResponse response = builder.build();
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

        createPostValidator = new CreatePostValidatorImpl();
        ReflectionTestUtils.setField( createPostValidator, "postTitleMinimumLength", 3 );
        ReflectionTestUtils.setField( createPostValidator, "postTitleMaximumLength", 256 );
        ReflectionTestUtils.setField( createPostValidator, "postTextMinimumLength", 3 );
        ReflectionTestUtils.setField( createPostValidator, "postTextMaximumLength", 4000 );

        String title = "Java Spring Boot";
        String text = "I";

        CreatePostResponseBuilder builder = CreatePostResponse.builder();
        HttpStatus status = createPostValidator.validate( builder, title, text );
        Assert.assertEquals( 422, status.value() );

        CreatePostResponse response = builder.build();
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

        createPostValidator = new CreatePostValidatorImpl();
        ReflectionTestUtils.setField( createPostValidator, "postTitleMinimumLength", 3 );
        ReflectionTestUtils.setField( createPostValidator, "postTitleMaximumLength", 7 );
        ReflectionTestUtils.setField( createPostValidator, "postTextMinimumLength", 3 );
        ReflectionTestUtils.setField( createPostValidator, "postTextMaximumLength", 4000 );

        String title = "Java Spring Boot";
        String text = "I want to learn Java Spring Boot.  Can anyone help?";

        CreatePostResponseBuilder builder = CreatePostResponse.builder();
        HttpStatus status = createPostValidator.validate( builder, title, text );
        Assert.assertEquals( 422, status.value() );

        CreatePostResponse response = builder.build();
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
