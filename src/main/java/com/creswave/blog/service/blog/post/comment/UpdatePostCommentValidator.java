package com.creswave.blog.service.blog.post.comment;

import org.springframework.http.HttpStatus;

import com.creswave.blog.domain.blog.Comment;
import com.creswave.blog.domain.blog.Post;
import com.creswave.blog.service.blog.post.comment.UpdatePostCommentResponse.UpdatePostCommentResponseBuilder;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public interface UpdatePostCommentValidator {

    HttpStatus validate(
            Post post, Comment commemt,
            UpdatePostCommentResponseBuilder builder,
            String text );
}
