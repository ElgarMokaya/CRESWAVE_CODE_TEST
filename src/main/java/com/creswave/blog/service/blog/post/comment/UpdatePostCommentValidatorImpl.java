package com.creswave.blog.service.blog.post.comment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.creswave.blog.domain.blog.Comment;
import com.creswave.blog.domain.blog.Post;
import com.creswave.blog.service.blog.post.comment.UpdatePostCommentResponse.UpdatePostCommentResponseBuilder;

import lombok.RequiredArgsConstructor;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Component
@RequiredArgsConstructor
public class UpdatePostCommentValidatorImpl implements UpdatePostCommentValidator {

    @Value( "${comment.text.validation.minimum-length}" )
    private Integer commentTextMinimumLength;

    @Value( "${comment.text.validation.maximum-length}" )
    private Integer commentTextMaximumLength;

    @Override
    public HttpStatus validate(
            Post post,
            Comment comment,
            UpdatePostCommentResponseBuilder builder,
            String text ) {

        HttpStatus status = HttpStatus.CREATED;
        Boolean hasValdationErrors = Boolean.FALSE;
        Boolean textToShort = Boolean.FALSE;
        Boolean textToLong = Boolean.FALSE;

        if ( post == null || comment == null ) {
            status = HttpStatus.NOT_FOUND;
        }
        else {

            if ( text == null || text.length() < commentTextMinimumLength ) {
                textToShort = Boolean.TRUE;
                hasValdationErrors = Boolean.TRUE;
            }

            if ( text != null && text.length() > commentTextMaximumLength ) {
                textToLong = Boolean.TRUE;
                hasValdationErrors = Boolean.TRUE;
            }

            if ( hasValdationErrors ) {
                status = HttpStatus.UNPROCESSABLE_ENTITY;
                text = null;
            }

            builder
                    .id( comment.getId() )
                    .text( text )
                    .hasValdationErrors( hasValdationErrors )
                    .textToShort( textToShort )
                    .textToLong( textToLong )
                    .commentTextMinimumLength( commentTextMinimumLength )
                    .commentTextMaximumLength( commentTextMaximumLength );
        }

        return status;
    }
}
