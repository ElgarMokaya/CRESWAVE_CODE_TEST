package com.creswave.blog.controller.blog;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.creswave.blog.domain.user.User;
import com.creswave.blog.helper.loggedin.LoggedInCredentialsHelper;
import com.creswave.blog.service.blog.BlogService;
import com.creswave.blog.service.blog.post.CreatePostRequest;
import com.creswave.blog.service.blog.post.CreatePostResponse;
import com.creswave.blog.service.blog.post.GetFilteredPostsResponse;
import com.creswave.blog.service.blog.post.GetPostResponse;
import  com.creswave.blog.service.blog.post.PostDTO;
import com.creswave.blog.service.blog.post.UpdatePostRequest;
import com.creswave.blog.service.blog.post.UpdatePostResponse;
import com.creswave.blog.service.blog.post.comment.CreatePostCommentRequest;
import com.creswave.blog.service.blog.post.comment.CreatePostCommentResponse;
import com.creswave.blog.service.blog.post.comment.UpdatePostCommentRequest;
import com.creswave.blog.service.blog.post.comment.UpdatePostCommentResponse;

import lombok.RequiredArgsConstructor;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@CrossOrigin
@RestController
@RequestMapping( "/api/v1/blog" )
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final LoggedInCredentialsHelper loggedInCredentialsHelper;

    // =============================================================================================
    // START: Create / Update / Get / Get Filtered / Delete Posts below
    //

    /**
     * Create a new Post.
     * 
     * @param request - Details to create the Post.
     * @return ResponseEntity<CreatePostResponse> - 
     * If successful (status code 201 Created), the response contains the created record. 
     * In the case of validation failure (status code 422 Unprocessable Entity), the response 
     * contains validation errors. 
     * In the case of insufficient privileges to call this API (status code 403 Forbidden), no 
     * response object is returned.  
     * In the case of the user not being authenticated (status code 401 Unauthorised) no response 
     * object is returned.
     */
    @PostMapping( "/posts" )
    @PreAuthorize( "hasAuthority('STANDARD_USER')" )
    public ResponseEntity<CreatePostResponse> createPost( @RequestBody CreatePostRequest request ) {

        User user = loggedInCredentialsHelper.getLoggedInUser();
        return blogService.createPost( user, request );
    }


    /**
     * Update an existing Post.
     * 
     * @param postId the post that needs updating.
     * @param request -Details to update the Post.
     * @return ResponseEntity<UpdatePostResponse> - 
     * If successful (status code 200 OK), the response contains the updated record.  
     * In the case of validation failure (status code 422 Unprocessable Entity), the response
     * contains validation errors.
     * In the case the record cannot be found (status code 404 Not Found), no response object is
     * returned.
     * In the case of insufficient privileges to call this API (status code 403 Forbidden), no 
     * response object is returned.
     * In the case of the user not being authenticated (status code 401 Unauthorised) no response 
     * object is returned.
     */
    @PutMapping( "/posts/{postId}" )
    @PreAuthorize( "hasAuthority('STANDARD_USER')" )
    public ResponseEntity<UpdatePostResponse> updatePost( @PathVariable Long postId,
            @RequestBody UpdatePostRequest request ) {

        User loggedinUser = loggedInCredentialsHelper.getLoggedInUser();
        return blogService.updatePost( loggedinUser, postId, request );
    }


    /**
     * Delete Post - 
     * If successfully deleted (status code 204 No Content). 
     * In the case the record cannot be found (status code 404 Not Found).
     * In the case of insufficient privileges to call this API (status code 403 Forbidden). 
     * In the case of the user not being authenticated (status code 401 Unauthorised). 
     * In all cases no response object is returned.
     * 
     * @param postId - the id of the post that can be deleted by the Admin Role
     */
    @DeleteMapping( "/posts/{postId}" )
    @PreAuthorize( "hasAuthority('DELETE_POST')" )
    public ResponseEntity<Void> deletePost( @PathVariable Long postId ) {

        return blogService.deletePost( postId );
    }


    /**
     * Retrieve Post and its comments.
     * 
     * @param postId - the id of the post to be retrieved.
     * @return ResponseEntity<GetPostResponse> -
     * If successful (status code 200 OK), the response contains the updated record.
     * In the case the record cannot be found (status code 404 Not Found) and no response object 
     * is returned.
     * In the case of insufficient privileges to call this API (status code 403 Forbidden) and no
     * response object is returned.  
     * In the case of the user not being authenticated (status code 401 Unauthorised) and no 
     * response object is returned.
     */
    @GetMapping( "/posts/{postId}" )
    @PreAuthorize( "hasAuthority('STANDARD_USER')" )
    public ResponseEntity<GetPostResponse> getPost( @PathVariable Long postId ) {

        return blogService.getPost( postId );
    }


    /**
     * Retrieve filtered Posts by title
     * 
     * @param title - the text to search for in Post titles.
     * @param text - the text to search for in Post content text.
     * @param pageNo - the page number
     * @param pageSize - the number of records per page
     * @param sortBy - the field names to sort by
     * @return ResponseEntity<GetFilteredPostsResponse[]> -
     * If successful (status code 200 OK), the response contains the filtered records.
     * In the case no matching records can be found (status code 404 Not Found) and an empty array 
     * is returned.
     * In the case of insufficient privileges to call this API (status code 403 Forbidden) and no
     * response object is returned.  
     * In the case of the user not being authenticated (status code 401 Unauthorised) and no 
     * response object is returned.
     */
    @GetMapping( "/posts-by-title" )
    @PreAuthorize( "hasAuthority('STANDARD_USER')" )
    public ResponseEntity<GetFilteredPostsResponse[]> getFilteredPostsByTitle(
            @RequestParam String title,
            @RequestParam( defaultValue = "0" ) Integer pageNo,
            @RequestParam( defaultValue = "10" ) Integer pageSize,
            @RequestParam( defaultValue = "id" ) String sortBy ) {

        // @todo: need integration test
        return null;
    }


    /**
     * Retrieve filtered Posts by title
     * 
     * @param text - the text to search for in Post content text.
     * @param pageNo - the page number
     * @param pageSize - the number of records per page
     * @param sortBy - the field names to sort by
     * @return Response
     * 
     * @return ResponseEntity<GetFilteredPostsResponse[]> -
     * If successful (status code 200 OK), the response contains the filtered records.
     * In the case no matching records can be found (status code 404 Not Found) and an empty array 
     * is returned.
     * In the case of insufficient privileges to call this API (status code 403 Forbidden) and no
     * response object is returned.  
     * In the case of the user not being authenticated (status code 401 Unauthorised) and no 
     * response object is returned.
     */
    @GetMapping( "/posts-by-text" )
    @PreAuthorize( "hasAuthority('STANDARD_USER')" )
    public ResponseEntity<PostDTO[]> getFilteredPostsByText(
            @RequestParam String text,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam( defaultValue = "id" ) String sortBy ) {

        // @todo: need integration test
        return null;
    }


    /**
     * Retrieve all posts with pagination and sorting
     * 
     * @param text - the text to search for in Post content text.
     * @param pageNo - the page number
     * @param pageSize - the number of records per page
     * @param sortBy - the field names to sort by
     * @return ResponseEntity<List<PostDTO>> -
     * If successful (status code 200 OK), the response contains the records.
     * In the case of insufficient privileges to call this API (status code 403 Forbidden) and no
     * response object is returned.  
     * In the case of the user not being authenticated (status code 401 Unauthorised) and no 
     * response object is returned.
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> getAllPosts(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam( defaultValue = "id" ) String sortBy ) {

        return blogService.getAllPosts( pageNo, pageSize, sortBy );
    }

    //
    // END: Create / Update / Get / Get Filtered / Delete Posts below
    // =============================================================================================

    // =============================================================================================
    // START: Create / Update / Get / Delete Comments for Posts below
    //


    /**
     * Create new Post Comment
     * 
     * @param request - details to create Post Comment. 
     * @return ResponseEntity<CreatePostCommentRequest> - 
     * If successful (status code 201 Created), the response contains the created record. 
     * In the case of validation failure (status code 422 Unprocessable Entity), the response 
     * contains validation errors. 
     * In the case no matching post record can be found (status code 404 Not Found) and no response
     * object is returned.
     * In the case of insufficient privileges to call this API (status code 403 Forbidden), no 
     * response object is returned.  
     * In the case of the user not being authenticated (status code 401 Unauthorised) no response 
     * object is returned.
     */
    @PostMapping( "/posts/{postId}/comments" )
    @PreAuthorize( "hasAuthority('STANDARD_USER')" )
    public ResponseEntity<CreatePostCommentResponse> createPostComment(
            @PathVariable Long postId,
            @RequestBody CreatePostCommentRequest request ) {

        User user = loggedInCredentialsHelper.getLoggedInUser();
        return blogService.createPostComment( user, postId, request );
    }


    /**
    * Update an existing Post Comment.
    * 
    * @param postId the post that needs updating.
    * @param commentId the comment of the post that needs updating.
    * @param request -Details to update the Post.
    * @return ResponseEntity<UpdatePostCommentResponse> - 
    * If successful (status code 200 OK), the response contains the updated record.  
    * In the case of validation failure (status code 422 Unprocessable Entity), the response
    * contains validation errors.
    * In the case the comment record cannot be found (status code 404 Not Found), no response object 
    * is returned.
    * In the case of insufficient privileges to call this API (status code 403 Forbidden), no 
    * response object is returned.
    * In the case of the user not being authenticated (status code 401 Unauthorised) no response 
    * object is returned.
    */
    @PutMapping( "/posts/{postId}/comments/{commentId}" )
    @PreAuthorize( "hasAuthority('STANDARD_USER')" )
    public ResponseEntity<UpdatePostCommentResponse> updatePostComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody UpdatePostCommentRequest request ) {

        User loggedInUser = loggedInCredentialsHelper.getLoggedInUser();
        return blogService.updatePostComment( loggedInUser, postId, commentId, request );
    }



    /**
     * Delete Post Comment - 
     * If successfully deleted (status code 204 No Content). 
     * In the case the record cannot be found (status code 404 Not Found).
     * In the case of insufficient privileges to call this API (status code 403 Forbidden). 
     * In the case of the user not being authenticated (status code 401 Unauthorised). 
     * In all cases no response object is returned.
     * 
     * @param postId - the id of the post that can be deleted by the Admin Role
     */
    @DeleteMapping( "/posts/{postId}/comments/{commentId}" )
    @PreAuthorize( "hasAuthority('STANDARD_USER')" )
    public ResponseEntity<Void> deletePostComment( @PathVariable Long postId,
            @PathVariable Long commentId ) {

        User loggedInUser = loggedInCredentialsHelper.getLoggedInUser();
        return blogService.deletePostComment( loggedInUser, postId, commentId );
    }

    //
    // END: Create / Update / Get / Get Filtered / Delete Comments for Posts below
    // =============================================================================================
}
