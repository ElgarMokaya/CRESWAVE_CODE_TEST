package com.creswave.blog.service.account.register;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.creswave.blog.domain.user.Role;
import com.creswave.blog.domain.user.RoleGroup;
import com.creswave.blog.domain.user.RoleGroupEnum;
import com.creswave.blog.domain.user.User;
import com.creswave.blog.helper.email.EmailHelper;
import com.creswave.blog.helper.hash.HashHelper;
import com.creswave.blog.helper.password.PasswordHelper;
import com.creswave.blog.helper.string.StringHelper;
import com.creswave.blog.repository.user.RoleGroupRepository;
import com.creswave.blog.repository.user.UserRepository;
import com.creswave.blog.service.account.register.email.RegisterRequest;
import com.creswave.blog.service.account.register.email.RegisterResponse;

import lombok.RequiredArgsConstructor;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final UserRepository userRepository;
    private final RoleGroupRepository roleGroupRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordHelper passwordHelper;
    private final HashHelper hashHelper;
    private final StringHelper stringHelper;
    private final EmailHelper emailHelper;

    @Value( "${username.validation.minimum-length}" )
    private Integer usernameMinimumLength;

    @Value( "${username.validation.maximum-length}" )
    private Integer usernameMaximumLength;

    @Value( "${password.validation.minimum-length}" )
    private Integer passwordMinimumLength;

    @Value( "${password.validation.needs-mixed-case}" )
    private Boolean passwordNeedsMixedCase;

    @Value( "${password.validation.needs-special-characters}" )
    private Boolean passwordNeedsSpecialCharacters;

    @Value( "${password.validation.needs-numbers}" )
    private Boolean passwordNeedsNumbers;

    @Value( "${password.validation.needs-letters}" )
    private Boolean passwordNeedsLetters;

    private Set<Role> getRoles( RoleGroupEnum roleGroupEnum ) {

        RoleGroup roleGroup =
                roleGroupRepository.findById( roleGroupEnum.getId() ).get();

        return roleGroup.getRoles();
    }


    @Transactional
    @Override
    public RegisterResponse register(
            RegisterRequest request, RoleGroupEnum roleGroupEnum ) {

        Set<Role> roles = getRoles( roleGroupEnum );
        String email = stringHelper.trimAndLowerCase( request.getEmail() );
        String username = stringHelper.trimAndLowerCase( request.getUsername() );
        String password = stringHelper.trim( request.getPassword() );
        String passwordAlt = stringHelper.trim( request.getPasswordAlt() );

        Boolean emailInvalid = Boolean.FALSE;
        Boolean emailExists = Boolean.FALSE;
        Boolean usernameTaken = Boolean.FALSE;
        Boolean success = Boolean.TRUE;
        Boolean usernameTooShort = Boolean.FALSE;
        Boolean usernameTooLong = Boolean.FALSE;
        Boolean passwordInvalid = Boolean.FALSE;
        Boolean passwordsDoNotMatch = Boolean.FALSE;

        if ( username == null || username.length() < usernameMinimumLength ) {

            usernameTooShort = Boolean.TRUE;
            success = Boolean.FALSE;
        }

        if ( username != null && username.length() > usernameMaximumLength ) {

            usernameTooLong = Boolean.TRUE;
            success = Boolean.FALSE;
        }

        if ( !emailHelper.emailValid( email ) ) {

            emailInvalid = Boolean.TRUE;
            success = Boolean.FALSE;
        }

        if ( !emailInvalid && userRepository.findByEmail( email ).isPresent() ) {

            emailExists = Boolean.TRUE;
            success = Boolean.FALSE;
        }

        if ( !usernameTooShort && userRepository.findByUsername( username ).isPresent() ) {

            emailExists = Boolean.TRUE;
            success = Boolean.FALSE;
        }

        if ( password != null && !password.equals( passwordAlt ) ) {

            passwordsDoNotMatch = Boolean.TRUE;
            success = Boolean.FALSE;
        }
        else {

            Boolean passwordValid = passwordHelper.passwordValid( password );
            passwordInvalid = !passwordValid;

            if ( passwordInvalid ) {

                success = Boolean.FALSE;
            }
        }


        if ( success ) {

            User user = User.builder()
                    .email( email )
                    .passwordHash( passwordEncoder.encode( password ) )
                    .username( username )
                    .active( Boolean.TRUE ) // Usually set to true when have activated with email
                    .build();

            user.getRoles().addAll( roles );
            user.setEmailActivationCode( hashHelper.getRandomHash() );
            User savedUser = userRepository.save( user );
        }

        return RegisterResponse.builder()
                .usernameTooShort( usernameTooShort )
                .usernameTooLong( usernameTooLong )
                .emailInvalid( emailInvalid )
                .emailExists( emailExists )
                .usernameTaken( usernameTaken )
                .passwordsDoNotMatch( passwordsDoNotMatch )
                .passwordInvalid( passwordInvalid )
                .passwordMinimumLength( passwordMinimumLength )
                .passwordNeedsLetters( passwordNeedsLetters )
                .passwordNeedsNumbers( passwordNeedsNumbers )
                .passwordNeedsMixedCase( passwordNeedsMixedCase )
                .passwordNeedsSpecialCharacters( passwordNeedsSpecialCharacters )
                .usernameMinimumLength( usernameMinimumLength )
                .success( success )
                .build();
    }
}
