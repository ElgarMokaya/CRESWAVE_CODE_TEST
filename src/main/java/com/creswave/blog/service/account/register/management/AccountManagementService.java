package com.creswave.blog.service.account.register.management;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.creswave.blog.domain.user.User;

/**
 * @author Elgar Mokaya - 31 Mar 2024
 */
@Service
public interface AccountManagementService {

    ResponseEntity<UpdateUsernameResponse> updateUsername( User loggedInUser,
            UpdateUsernameRequest request );


    ResponseEntity<UpdatePasswordResponse> updatePassword( User loggedInUser,
            UpdatePasswordRequest request );
}
