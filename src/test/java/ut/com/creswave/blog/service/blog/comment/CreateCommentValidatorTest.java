package ut.com.creswave.blog.service.blog.comment;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import com.creswave.blog.domain.blog.Post;
import com.creswave.blog.service.blog.post.comment.CreatePostCommentResponse;
import com.creswave.blog.service.blog.post.comment.CreatePostCommentResponse.CreatePostCommentResponseBuilder;
import com.creswave.blog.service.blog.post.comment.CreatePostCommentValidator;
import com.creswave.blog.service.blog.post.comment.CreatePostCommentValidatorImpl;


/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public class CreateCommentValidatorTest {

    private static CreatePostCommentValidator createPostCommentValidator;

    @Test
    public void createPostValid() {

        createPostCommentValidator = new CreatePostCommentValidatorImpl();
        ReflectionTestUtils.setField( createPostCommentValidator, "commentTextMinimumLength", 3 );
        ReflectionTestUtils.setField( createPostCommentValidator, "commentTextMaximumLength",
                2000 );
        
        CreatePostCommentResponseBuilder builder = CreatePostCommentResponse.builder();
        String postTitle = "Java Spring Boot";
        String postText = "I want to learn Java Spring Boot.  Can anyone help?";
        String commentText = "I can help. My github username is: elgarmokaya";

        Post post = Post.builder().id( 10000l ).title( postTitle ).text( postText ).build();
        HttpStatus status = createPostCommentValidator.validate( post, builder, commentText );
        Assert.assertEquals( 201, status.value() );

        CreatePostCommentResponse response = builder.build();
        Assert.assertFalse( response.getHasValdationErrors() );
        Assert.assertFalse( response.getTextToShort() );
        Assert.assertFalse( response.getTextToLong() );
        Assert.assertNotNull( response.getCommentTextMinimumLength() );
        Assert.assertNotNull( response.getCommentTextMaximumLength() );
    }


    @Test
    public void postNotFound() {

        createPostCommentValidator = new CreatePostCommentValidatorImpl();

        ReflectionTestUtils.setField( createPostCommentValidator, "commentTextMinimumLength", 3 );
        ReflectionTestUtils.setField( createPostCommentValidator, "commentTextMaximumLength",
                2000 );

        String commentText = "I can help. My github username is: elgarmokaya";

        CreatePostCommentResponseBuilder builder = CreatePostCommentResponse.builder();
        Post post = null;

        HttpStatus status = createPostCommentValidator.validate( post, builder, commentText );
        Assert.assertEquals( 404, status.value() );
    }


    @Test
    public void textTooShort() {

        createPostCommentValidator = new CreatePostCommentValidatorImpl();
        ReflectionTestUtils.setField( createPostCommentValidator, "commentTextMinimumLength", 3 );

        ReflectionTestUtils.setField( createPostCommentValidator, "commentTextMaximumLength",
                2000 );

        String postTitle = "Java Spring Boot";
        String postText = "I want to learn Java Spring Boot.  Can anyone help?";
        String commentText = "I";

        CreatePostCommentResponseBuilder builder = CreatePostCommentResponse.builder();
        Post post = Post.builder().id( 10000l ).title( postTitle ).text( postText ).build();

        HttpStatus status = createPostCommentValidator.validate( post, builder, commentText );
        Assert.assertEquals( 422, status.value() );

        CreatePostCommentResponse response = builder.build();
        Assert.assertTrue( response.getHasValdationErrors() );
        Assert.assertTrue( response.getTextToShort() );
        Assert.assertFalse( response.getTextToLong() );
        Assert.assertNotNull( response.getCommentTextMinimumLength() );
        Assert.assertNotNull( response.getCommentTextMaximumLength() );
    }


    @Test
    public void textTooLong() {

        createPostCommentValidator = new CreatePostCommentValidatorImpl();

        ReflectionTestUtils.setField( createPostCommentValidator, "commentTextMinimumLength", 3 );
        ReflectionTestUtils.setField( createPostCommentValidator, "commentTextMaximumLength", 5 );

        String postTitle = "Java Spring Boot";
        String postText = "I want to learn Java Spring Boot.  Can anyone help?";
        String commentText = "I can help. My github username is: elgarmokaya";

        CreatePostCommentResponseBuilder builder = CreatePostCommentResponse.builder();
        Post post = Post.builder().id( 10000l ).title( postTitle ).text( postText ).build();

        HttpStatus status = createPostCommentValidator.validate( post, builder, commentText );
        Assert.assertEquals( 422, status.value() );

        CreatePostCommentResponse response = builder.build();
        Assert.assertTrue( response.getHasValdationErrors() );
        Assert.assertFalse( response.getTextToShort() );
        Assert.assertTrue( response.getTextToLong() );
        Assert.assertNotNull( response.getCommentTextMinimumLength() );
        Assert.assertNotNull( response.getCommentTextMaximumLength() );
    }
}
