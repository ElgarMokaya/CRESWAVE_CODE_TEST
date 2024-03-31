package com.creswave.blog.service.blog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.creswave.blog.domain.blog.Comment;
import com.creswave.blog.domain.blog.Post;
import com.creswave.blog.domain.user.User;
import com.creswave.blog.helper.string.StringHelper;
import com.creswave.blog.repository.comment.CommentRepository;
import com.creswave.blog.repository.post.PostRepository;
import com.creswave.blog.service.blog.post.CommentDTO;
import com.creswave.blog.service.blog.post.CreatePostRequest;
import com.creswave.blog.service.blog.post.CreatePostResponse;
import com.creswave.blog.service.blog.post.CreatePostResponse.CreatePostResponseBuilder;
import com.creswave.blog.service.blog.post.CreatePostValidator;
import com.creswave.blog.service.blog.post.GetPostResponse;
import com.creswave.blog.service.blog.post.PostDTO;
import com.creswave.blog.service.blog.post.UpdatePostRequest;
import com.creswave.blog.service.blog.post.UpdatePostResponse;
import com.creswave.blog.service.blog.post.UpdatePostResponse.UpdatePostResponseBuilder;
import com.creswave.blog.service.blog.post.UpdatePostValidator;
import com.creswave.blog.service.blog.post.comment.CreatePostCommentRequest;
import com.creswave.blog.service.blog.post.comment.CreatePostCommentResponse;
import com.creswave.blog.service.blog.post.comment.CreatePostCommentResponse.CreatePostCommentResponseBuilder;
import com.creswave.blog.service.blog.post.comment.CreatePostCommentValidator;
import com.creswave.blog.service.blog.post.comment.UpdatePostCommentRequest;
import com.creswave.blog.service.blog.post.comment.UpdatePostCommentResponse;
import com.creswave.blog.service.blog.post.comment.UpdatePostCommentResponse.UpdatePostCommentResponseBuilder;
import com.creswave.blog.service.blog.post.comment.UpdatePostCommentValidator;
import com.creswave.blog.service.shared.MaliciousException;

import lombok.RequiredArgsConstructor;


/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private Logger logger = LoggerFactory.getLogger( BlogServiceImpl.class );

    private final StringHelper stringHelper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CreatePostValidator createPostValidator;
    private final UpdatePostValidator updatePostValidator;
    private final CreatePostCommentValidator createPostCommentValidator;
    private final UpdatePostCommentValidator updatePostCommentValidator;


    private List<CommentDTO> createCommentDTOs( List<Comment> comments ) {

        List<CommentDTO> commentDTOs = new ArrayList<>();

        for ( Comment comment : comments ) {
            commentDTOs.add(
                    CommentDTO.builder()
                            .id( comment.getId() )
                            .text( comment.getText() )
                            .build() );
        }

        return commentDTOs;
    }


    private PostDTO createPostDTO( Post post ) {

        PostDTO postDTO =
                PostDTO.builder()
                        .id( post.getId() )
                        .title( post.getTitle() )
                        .text( post.getText() )
                        .comments( createCommentDTOs( post.getComments() ) )
                        .build();

        return postDTO;
    }


    @Transactional
    @Override
    public ResponseEntity<List<PostDTO>> getAllPosts( Integer pageNo, Integer pageSize,
            String sortBy ) {

        Pageable paging = PageRequest.of( pageNo, pageSize, Sort.by( sortBy ) );

        Page<Post> pagePosts = postRepository.findAll( paging );
        List<PostDTO> postDTOs = new ArrayList<>();

        if ( pagePosts.hasContent() ) {
            List<Post> posts = pagePosts.getContent();

            for ( Post post : posts ) {
                postDTOs.add( createPostDTO( post ) );
            }
        }

        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status( status ).body( postDTOs );
    }


    @Transactional
    @Override
    public ResponseEntity<CreatePostResponse> createPost( User user, CreatePostRequest request ) {

        String title = stringHelper.trim( request.getTitle() );
        String text = stringHelper.trim( request.getText() );

        CreatePostResponseBuilder responseBuilder = CreatePostResponse.builder();
        HttpStatus status = createPostValidator.validate( responseBuilder, title, text );

        if ( !responseBuilder.build().getHasValdationErrors() ) {
            Post post =
                    Post.builder()
                            .title( title )
                            .text( text )
                            .user( user )
                            .build();

            Post savedPost = postRepository.save( post );
            Long id = savedPost.getId();
            responseBuilder.id( id );
        }

        return ResponseEntity.status( status ).body( responseBuilder.build() );
    }


    @Transactional
    @Override
    public ResponseEntity<UpdatePostResponse> updatePost(
            User loggedInUser, Long postId, UpdatePostRequest request ) {

        String title = stringHelper.trim( request.getTitle() );
        String text = stringHelper.trim( request.getText() );

        UpdatePostResponseBuilder responseBuilder = UpdatePostResponse.builder();
        
        Optional<Post> postOptional = postRepository.findById( postId );
        Post post = null;

        if ( postOptional.isPresent() ) {
            post = postOptional.get();
        }

        if ( !loggedInUser.getId().equals( post.getUser().getId() ) ) {
                
            throw new MaliciousException( 
                    "Logged in User with id: " + loggedInUser.getId() + 
                    " tried to delete a post which belonged to another user with id " + 
                            post.getUser().getId() );
        }

        HttpStatus status = updatePostValidator.validate( post, responseBuilder, title, text );
        UpdatePostResponse updatePostResponse = responseBuilder.build();

        if ( !updatePostResponse.getHasValdationErrors() ) {

            post.setTitle( title );
            post.setText( text );
            Post savedPost = postRepository.save( post );
            logger.info( savedPost.toString() );
        }

        return ResponseEntity.status( status ).body( updatePostResponse );
    }


    @Override
    public ResponseEntity<GetPostResponse> getPost( Long postId ) {

        HttpStatus status = HttpStatus.OK;
        GetPostResponse getPostResponse = null;

        Optional<Post> postOptional = postRepository.findById( postId );

        if ( postOptional.isPresent() ) {

            Post post = postOptional.get();

            List<Comment> comments = post.getComments();
            List<CommentDTO> commentDTOs = createCommentDTOs( comments );

            getPostResponse =
                    GetPostResponse.builder()
                            .id( post.getId() )
                            .title( post.getTitle() )
                            .text( post.getText() )
                            .comments( commentDTOs )
                            .build();
        }
        else {
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status( status ).body( getPostResponse );
    }


    @Transactional
    @Override
    public ResponseEntity<CreatePostCommentResponse> createPostComment(
            User loggedInUser, Long postId, CreatePostCommentRequest request ) {

        Optional<Post> postOptional = postRepository.findById( postId );
        Post post = null;

        if ( postOptional.isPresent() ) {
            post = postOptional.get();
        }

        String text = request.getText();

        CreatePostCommentResponseBuilder responseBuilder = CreatePostCommentResponse.builder();
        HttpStatus status = createPostCommentValidator.validate( post, responseBuilder, text );

        if ( !responseBuilder.build().getHasValdationErrors() ) {
            Comment comment =
                    Comment.builder()
                            .text( text )
                            .post( post )
                            .user( loggedInUser )
                            .build();

            Comment saveComment = commentRepository.save( comment );
            Long id = saveComment.getId();
            responseBuilder.id( id );
        }

        return ResponseEntity.status( status ).body( responseBuilder.build() );
    }


    @Transactional
    @Override
    public ResponseEntity<UpdatePostCommentResponse> updatePostComment(
            User loggedInUser, Long postId, Long commentId, UpdatePostCommentRequest request ) {

        Optional<Comment> commentOptional = commentRepository.findById( commentId );

        Optional<Post> postOptional = postRepository.findById( postId );
        Post post = null;
        Comment comment = null;

        if ( commentOptional.isPresent() ) {
            comment = commentOptional.get();
        }

        if ( postOptional.isPresent() ) {
            post = postOptional.get();
        }

        if ( comment != null && !loggedInUser.getId().equals( comment.getUser().getId() ) ) {

            throw new MaliciousException(
                    "Logged in User with id: " + loggedInUser.getId() +
                            " tried to update a comment which belonged to another user with id " +
                            comment.getUser().getId() );
        }

        if ( comment != null && post != null && !comment.getPost().getId().equals( post
                .getId() ) ) {

            throw new MaliciousException(
                    "Logged in User with id: " + loggedInUser.getId() +
                            " tried to update a comment which does not belong to the Post" +
                            " id passed in" );
        }


        String text = request.getText();

        UpdatePostCommentResponseBuilder responseBuilder = UpdatePostCommentResponse.builder();

        HttpStatus status =
                updatePostCommentValidator.validate( post, comment, responseBuilder, text );

        if ( !responseBuilder.build().getHasValdationErrors() ) {
            comment.setText( text );
            Comment saveComment = commentRepository.save( comment );
            logger.info( saveComment.toString() );
        }

        return ResponseEntity.status( status ).body( responseBuilder.build() );
    }


    @Transactional
    @Override
    public ResponseEntity<Void> deletePost( Long postId ) {
        
        HttpStatus status = HttpStatus.NO_CONTENT;
        
        Optional<Post> optionalPost = postRepository.findById( postId );


        if ( !optionalPost.isPresent() ) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
            Post post = optionalPost.get();

            List<Comment> comments = post.getComments();
            
            if ( comments.size() > 0 ) {
                commentRepository.deleteAll( comments );
            }
            
            postRepository.delete( post );
        }

        return ResponseEntity.status( status ).build();
    }


    @Override
    public ResponseEntity<Void> deletePostComment(
            User loggedInUser, Long postId, Long commentId ) {

        HttpStatus status = HttpStatus.NO_CONTENT;
        Optional<Comment> commentOptional = commentRepository.findById( commentId );
        Optional<Post> postOptional = postRepository.findById( postId );
        Post post = null;
        Comment comment = null;

        if ( commentOptional.isPresent() ) {
            comment = commentOptional.get();
        }

        if ( postOptional.isPresent() ) {
            post = postOptional.get();
        }

        if ( comment != null && !loggedInUser.getId().equals( comment.getUser().getId() ) ) {

            throw new MaliciousException(
                    "Logged in User with id: " + loggedInUser.getId() +
                            " tried to delete a comment which belonged to another user with id " +
                            comment.getUser().getId() );
        }

        if ( comment != null && post != null && !comment.getPost().getId().equals( post
                .getId() ) ) {

            throw new MaliciousException(
                    "Logged in User with id: " + loggedInUser.getId() +
                            " tried to delete a comment which does not belong to the Post" +
                            " id passed in" );
        }

        commentRepository.delete( comment );
        return ResponseEntity.status( status ).build();
    }
}
