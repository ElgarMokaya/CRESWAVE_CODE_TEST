package com.creswave.blog.service.blog.post.comment;

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
public class CreatePostCommentResponse {

    private Long id;
    private String text;

    private Boolean hasValdationErrors;
    private Boolean textToShort;
    private Boolean textToLong;
    private Integer commentTextMinimumLength;
    private Integer commentTextMaximumLength;
}
