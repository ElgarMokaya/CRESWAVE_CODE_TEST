package com.creswave.blog.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.creswave.blog.domain.blog.Comment;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
