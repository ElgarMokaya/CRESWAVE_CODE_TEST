package com.creswave.blog.controller.account.login;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creswave.blog.service.login.email.LoginRequest;
import com.creswave.blog.service.login.email.LoginResponse;
import com.creswave.blog.service.login.email.LoginService;

import lombok.RequiredArgsConstructor;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@CrossOrigin
@RestController
@RequestMapping( "/api/v1/login" )
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping( "/authenticate" )
    public ResponseEntity<LoginResponse> authenticate(
            @RequestBody LoginRequest request ) {

        LoginResponse response = loginService.authenticate( request );
        return ResponseEntity.ok( response );
    }
}
