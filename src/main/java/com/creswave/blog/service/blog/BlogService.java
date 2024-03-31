package com.creswave.blog.service.blog;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.creswave.blog.domain.user.User;
import com.creswave.blog.service.blog.post.CreatePostRequest;
import com.creswave.blog.service.blog.post.CreatePostResponse;
import com.creswave.blog.service.blog.post.GetPostResponse;
import com.creswave.blog.service.blog.post.PostDTO;
import com.creswave.blog.service.blog.post.UpdatePostRequest;
import com.creswave.blog.service.blog.post.UpdatePostResponse;
import com.creswave.blog.service.blog.post.comment.CreatePostCommentRequest;
import com.creswave.blog.service.blog.post.comment.CreatePostCommentResponse;
import com.creswave.blog.service.blog.post.comment.UpdatePostCommentRequest;
import com.creswave.blog.service.blog.post.comment.UpdatePostCommentResponse;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public interface BlogService {

    ResponseEntity<CreatePostResponse> createPost( User user, CreatePostRequest request );


    ResponseEntity<UpdatePostResponse> updatePost( User user, Long postId,
            UpdatePostRequest request );


    ResponseEntity<CreatePostCommentResponse> createPostComment( User user, Long postId,
            CreatePostCommentRequest request );


    ResponseEntity<UpdatePostCommentResponse> updatePostComment( User loggedInUser, Long postId,
            Long commentId, UpdatePostCommentRequest request );


    ResponseEntity<GetPostResponse> getPost( Long postId );


    ResponseEntity<Void> deletePost( Long postId );


    ResponseEntity<Void> deletePostComment(
            User loggedInUser, Long postId, Long commentId );


    ResponseEntity<List<PostDTO>> getAllPosts( Integer pageNo, Integer pageSize, String sortBy );
}