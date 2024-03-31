package com.creswave.blog.service.blog.post;

import org.springframework.http.HttpStatus;

import com.creswave.blog.domain.blog.Post;
import com.creswave.blog.service.blog.post.UpdatePostResponse.UpdatePostResponseBuilder;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public interface UpdatePostValidator {

    HttpStatus validate( Post post, UpdatePostResponseBuilder builder, String title, String text );
}
