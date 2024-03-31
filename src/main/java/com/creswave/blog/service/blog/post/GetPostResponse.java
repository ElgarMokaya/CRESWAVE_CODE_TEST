package com.creswave.blog.service.blog.post;

import java.util.List;

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
public class GetPostResponse {

    private Long id;
    private String title;
    private String text;

    private List<CommentDTO> comments;
}
