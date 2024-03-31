package com.creswave.blog.controller.account.management;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creswave.blog.domain.user.User;
import com.creswave.blog.helper.loggedin.LoggedInCredentialsHelper;
import com.creswave.blog.service.account.register.management.AccountManagementService;
import com.creswave.blog.service.account.register.management.UpdatePasswordRequest;
import com.creswave.blog.service.account.register.management.UpdatePasswordResponse;
import com.creswave.blog.service.account.register.management.UpdateUsernameRequest;
import com.creswave.blog.service.account.register.management.UpdateUsernameResponse;

import lombok.RequiredArgsConstructor;

/**
 * @author Elgar Mokaya - 31 Mar 2024
 */
@CrossOrigin
@RestController
@RequestMapping( "/api/v1/account" )
@RequiredArgsConstructor
public class AccountManagementController {

    private final LoggedInCredentialsHelper loggedInCredentialsHelper;
    private final AccountManagementService accountManagementService;

    @PatchMapping( "/update-username/{userId}" )
    @PreAuthorize( "hasAuthority('STANDARD_USER')" )
    public ResponseEntity<UpdateUsernameResponse> updateUsername( @PathVariable Long userId,
            @RequestBody UpdateUsernameRequest request ) {

        // @todo: needs integration test
        User loggedinUser = loggedInCredentialsHelper.getLoggedInUser();
        return accountManagementService.updateUsername( loggedinUser, request );
    }


    @PatchMapping( "/update-password/{userId}" )
    @PreAuthorize( "hasAuthority('STANDARD_USER')" )
    public ResponseEntity<UpdatePasswordResponse> updatePost( @PathVariable Long userId,
            @RequestBody UpdatePasswordRequest request ) {

        // @todo: needs integration test
        User loggedinUser = loggedInCredentialsHelper.getLoggedInUser();
        return accountManagementService.updatePassword( loggedinUser, request );
    }
}
