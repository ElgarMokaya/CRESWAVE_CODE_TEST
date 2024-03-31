package com.creswave.blog.controller.account.logout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creswave.blog.service.logout.LogoutResponse;
import com.creswave.blog.service.logout.LogoutService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@CrossOrigin
@RestController
@RequestMapping( "/api/v1/logout" )
@RequiredArgsConstructor
public class LogoutController {

    private Logger logger = LoggerFactory.getLogger( LogoutController.class );

    @Autowired
    private LogoutService logoutService;

    @PostMapping( "/logout" )
    public ResponseEntity<LogoutResponse> logout(
            HttpServletRequest request, HttpServletResponse response ) {

        logger.info( "request = " + request );
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logoutService.logout( request, response, authentication );
        LogoutResponse logoutResponse = LogoutResponse.builder().success( Boolean.TRUE ).build();
        return ResponseEntity.ok( logoutResponse );
    }
}
