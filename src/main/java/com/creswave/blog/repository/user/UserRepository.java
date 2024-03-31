package com.creswave.blog.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.creswave.blog.domain.user.User;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail( String email );


    Optional<User> findByUsername( String username );


    Optional<User> findByEmailActivationCode( String emailActivatioCode );


    Optional<User> findByForgottenPasswordCode( String code );
}
