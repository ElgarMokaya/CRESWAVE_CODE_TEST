package com.creswave.blog.service.blog.post;

import org.springframework.http.HttpStatus;

import com.creswave.blog.service.blog.post.CreatePostResponse.CreatePostResponseBuilder;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public interface CreatePostValidator {

    HttpStatus validate( CreatePostResponseBuilder builder, String title, String text );
}
