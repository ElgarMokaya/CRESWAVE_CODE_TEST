package com.creswave.blog.service.blog.post;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.creswave.blog.domain.blog.Post;
import com.creswave.blog.service.blog.post.UpdatePostResponse.UpdatePostResponseBuilder;

import lombok.RequiredArgsConstructor;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Component
@RequiredArgsConstructor
public class UpdatePostValidatorImpl implements UpdatePostValidator {

    @Value( "${post.title.validation.minimum-length}" )
    private Integer postTitleMinimumLength;

    @Value( "${post.title.validation.maximum-length}" )
    private Integer postTitleMaximumLength;

    @Value( "${post.text.validation.minimum-length}" )
    private Integer postTextMinimumLength;

    @Value( "${post.text.validation.maximum-length}" )
    private Integer postTextMaximumLength;

    @Override
    public HttpStatus validate(
            Post post, UpdatePostResponseBuilder builder, String title, String text ) {

        HttpStatus status = HttpStatus.OK;
        Boolean hasValdationErrors = Boolean.FALSE;
        Boolean titleToShort = Boolean.FALSE;
        Boolean titleToLong = Boolean.FALSE;
        Boolean textToShort = Boolean.FALSE;
        Boolean textToLong = Boolean.FALSE;

        if ( post == null ) {
            status = HttpStatus.NOT_FOUND;
        }
        else {

            if ( title == null || title.length() < postTitleMinimumLength ) {
                titleToShort = Boolean.TRUE;
                hasValdationErrors = Boolean.TRUE;
            }

            if ( title != null && title.length() > postTitleMaximumLength ) {
                titleToLong = Boolean.TRUE;
                hasValdationErrors = Boolean.TRUE;
            }

            if ( text == null || text.length() < postTextMinimumLength ) {
                titleToShort = Boolean.TRUE;
                hasValdationErrors = Boolean.TRUE;
            }

            if ( text != null && text.length() > postTextMaximumLength ) {
                titleToLong = Boolean.TRUE;
                hasValdationErrors = Boolean.TRUE;
            }

            if ( hasValdationErrors ) {
                status = HttpStatus.UNPROCESSABLE_ENTITY;
                title = null;
                text = null;
            }

            builder
                    .id( post.getId() )
                    .title( title )
                    .text( text )
                    .hasValdationErrors( hasValdationErrors )
                    .titleToShort( titleToShort )
                    .titleToLong( titleToLong )
                    .textToShort( textToShort )
                    .textToLong( textToLong )
                    .postTitleMinimumLength( postTitleMinimumLength )
                    .postTitleMaximumLength( postTitleMaximumLength )
                    .postTextMinimumLength( postTextMinimumLength )
                    .postTextMaximumLength( postTextMaximumLength );
        }

        return status;
    }
}
