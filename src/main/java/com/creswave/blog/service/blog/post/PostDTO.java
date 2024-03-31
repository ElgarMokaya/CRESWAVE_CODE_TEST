package com.creswave.blog.service.blog.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private Long id;
    private String text;
    private String title;
    private List<CommentDTO> comments;
}
