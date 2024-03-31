package ut.com.creswave.blog.service.blog.comment;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import com.creswave.blog.domain.blog.Comment;
import com.creswave.blog.domain.blog.Post;
import com.creswave.blog.service.blog.post.comment.UpdatePostCommentResponse;
import com.creswave.blog.service.blog.post.comment.UpdatePostCommentResponse.UpdatePostCommentResponseBuilder;
import com.creswave.blog.service.blog.post.comment.UpdatePostCommentValidator;
import com.creswave.blog.service.blog.post.comment.UpdatePostCommentValidatorImpl;


/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public class UpdateCommentValidatorTest {

    private static UpdatePostCommentValidator updatePostCommentValidator;

    @Test
    public void uupdatePostValid() {

        updatePostCommentValidator = new UpdatePostCommentValidatorImpl();
        ReflectionTestUtils.setField( updatePostCommentValidator, "commentTextMinimumLength", 3 );
        ReflectionTestUtils.setField( updatePostCommentValidator, "commentTextMaximumLength",
                2000 );
        
        UpdatePostCommentResponseBuilder builder = UpdatePostCommentResponse.builder();
        String postTitle = "Java Spring Boot";
        String postText = "I want to learn Java Spring Boot.  Can anyone help?";

        String commentText =
                "I can help. My github username is: elgarmokaya.  Raise a ticket in one on my projects";

        Post post = Post.builder().id( 10000l ).title( postTitle ).text( postText ).build();

        Comment comment = Comment.builder().id( 10000l ).text( commentText ).build();

        HttpStatus status =
                updatePostCommentValidator.validate( post, comment, builder, commentText );

        Assert.assertEquals( 201, status.value() );

        UpdatePostCommentResponse response = builder.build();
        Assert.assertFalse( response.getHasValdationErrors() );
        Assert.assertFalse( response.getTextToShort() );
        Assert.assertFalse( response.getTextToLong() );
        Assert.assertNotNull( response.getCommentTextMinimumLength() );
        Assert.assertNotNull( response.getCommentTextMaximumLength() );
    }


    @Test
    public void commentOrPostNotFound() {

        updatePostCommentValidator = new UpdatePostCommentValidatorImpl();

        ReflectionTestUtils.setField( updatePostCommentValidator, "commentTextMinimumLength", 3 );

        ReflectionTestUtils.setField( updatePostCommentValidator, "commentTextMaximumLength",
                2000 );

        String commentText = "I can help. My github username is: elgarmokaya";

        UpdatePostCommentResponseBuilder builder = UpdatePostCommentResponse.builder();
        Post post = null;
        Comment comment = null;

        HttpStatus status =
                updatePostCommentValidator.validate( post, comment, builder, commentText );

        Assert.assertEquals( 404, status.value() );
    }


    @Test
    public void textTooShort() {

        updatePostCommentValidator = new UpdatePostCommentValidatorImpl();
        ReflectionTestUtils.setField( updatePostCommentValidator, "commentTextMinimumLength", 3 );

        ReflectionTestUtils.setField( updatePostCommentValidator, "commentTextMaximumLength",
                2000 );

        String postTitle = "Java Spring Boot";
        String postText = "I want to learn Java Spring Boot.  Can anyone help?";
        String commentText = "I";

        UpdatePostCommentResponseBuilder builder = UpdatePostCommentResponse.builder();
        Post post = Post.builder().id( 10000l ).title( postTitle ).text( postText ).build();
        Comment comment = Comment.builder().id( 10000l ).text( commentText ).build();

        HttpStatus status = updatePostCommentValidator.validate( post, comment, builder,
                commentText );

        Assert.assertEquals( 422, status.value() );

        UpdatePostCommentResponse response = builder.build();
        Assert.assertTrue( response.getHasValdationErrors() );
        Assert.assertTrue( response.getTextToShort() );
        Assert.assertFalse( response.getTextToLong() );
        Assert.assertNotNull( response.getCommentTextMinimumLength() );
        Assert.assertNotNull( response.getCommentTextMaximumLength() );
    }


    @Test
    public void textTooLong() {

        updatePostCommentValidator = new UpdatePostCommentValidatorImpl();

        ReflectionTestUtils.setField( updatePostCommentValidator, "commentTextMinimumLength", 3 );
        ReflectionTestUtils.setField( updatePostCommentValidator, "commentTextMaximumLength", 5 );

        String postTitle = "Java Spring Boot";
        String postText = "I want to learn Java Spring Boot.  Can anyone help?";
        String commentText = "I can help. My github username is: elgarmokaya";

        UpdatePostCommentResponseBuilder builder = UpdatePostCommentResponse.builder();
        Post post = Post.builder().id( 10000l ).title( postTitle ).text( postText ).build();
        Comment comment = Comment.builder().id( 10000l ).text( commentText ).build();

        HttpStatus status = updatePostCommentValidator.validate( post, comment, builder,
                commentText );
        Assert.assertEquals( 422, status.value() );

        UpdatePostCommentResponse response = builder.build();
        Assert.assertTrue( response.getHasValdationErrors() );
        Assert.assertFalse( response.getTextToShort() );
        Assert.assertTrue( response.getTextToLong() );
        Assert.assertNotNull( response.getCommentTextMinimumLength() );
        Assert.assertNotNull( response.getCommentTextMaximumLength() );
    }
}
