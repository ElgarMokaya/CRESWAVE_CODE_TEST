package com.creswave.blog.service.login.email;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public interface LoginService {

    LoginResponse authenticate( LoginRequest request );
}
