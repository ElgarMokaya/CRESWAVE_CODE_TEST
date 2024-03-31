package com.creswave.blog.service.blog.post.comment;

import org.springframework.http.HttpStatus;

import com.creswave.blog.domain.blog.Post;
import com.creswave.blog.service.blog.post.comment.CreatePostCommentResponse.CreatePostCommentResponseBuilder;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public interface CreatePostCommentValidator {

    HttpStatus validate( Post post, CreatePostCommentResponseBuilder builder, String text );
}
