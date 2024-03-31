package com.creswave.blog.helper.loggedin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.creswave.blog.domain.user.User;
import com.creswave.blog.repository.user.UserRepository;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Component
public class LoggedInCredentialsHelperImpl implements LoggedInCredentialsHelper {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getLoggedInUser() {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        SecurityContext securityContext2 =
                SecurityContextHolder.getContextHolderStrategy().getDeferredContext().get();

        if ( authentication == null || authentication instanceof AnonymousAuthenticationToken ) {

            return null;
        }

        String email = authentication.getName();
        User user = null;
        Optional<User> userOptional = userRepository.findByEmail( email );

        if ( userOptional.isPresent() ) {
            user = userOptional.get();
        }

        return user;
    }


    @Override
    public String getLoggedInUserEmail() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if ( authentication == null
                || authentication instanceof UsernamePasswordAuthenticationToken ) {

            return null;
        }
        else {

            String email = authentication.getName();
            return email;
        }
    }
}
