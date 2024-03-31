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
public class CommentDTO {

    private Long id;
    private String text;
}
