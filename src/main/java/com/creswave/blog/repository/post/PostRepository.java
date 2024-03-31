package com.creswave.blog.repository.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.creswave.blog.domain.blog.Post;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    // @todo: need integrating into services called by controller
    List<Post> findByTitleContains( String text );


    // @todo: need integrating into services called by controller
    List<Post> findByTextContains( String text );
}
