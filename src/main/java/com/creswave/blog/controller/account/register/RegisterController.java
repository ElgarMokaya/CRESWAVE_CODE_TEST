package com.creswave.blog.controller.account.register;

import org.apache.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creswave.blog.domain.user.RoleGroupEnum;
import com.creswave.blog.service.account.register.RegisterService;
import com.creswave.blog.service.account.register.email.RegisterRequest;
import com.creswave.blog.service.account.register.email.RegisterResponse;

import lombok.RequiredArgsConstructor;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@CrossOrigin
@RestController
@RequestMapping( "/api/v1/register" )
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;


    @PostMapping( "/save" )
    public ResponseEntity<RegisterResponse> registerUsingEmail(
            @RequestBody RegisterRequest request ) {

        RegisterResponse response = registerService.register( request,
                RoleGroupEnum.STANDARD_USER );

        // return 201
        return new ResponseEntity<>( response, HttpStatusCode.valueOf( HttpStatus.SC_CREATED ) );
    }
}
