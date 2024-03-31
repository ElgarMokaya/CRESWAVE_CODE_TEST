package com.creswave.blog.service.blog.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostResponse {

    private Long id;
    private String title;
    private String text;

    private Boolean hasValdationErrors;
    private Boolean titleToShort;
    private Boolean titleToLong;
    private Boolean textToShort;
    private Boolean textToLong;
    private Integer postTitleMinimumLength;
    private Integer postTitleMaximumLength;
    private Integer postTextMinimumLength;
    private Integer postTextMaximumLength;
}
