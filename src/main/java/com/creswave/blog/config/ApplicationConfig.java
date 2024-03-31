package com.creswave.blog.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.creswave.blog.domain.user.Role;
import com.creswave.blog.domain.user.User;
import com.creswave.blog.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private Logger logger = LoggerFactory.getLogger( ApplicationConfig.class );

    private final UserRepository repository;

    private Collection<? extends GrantedAuthority> createAuthorities( User user ) {

        List<SimpleGrantedAuthority> simpleGrantedAuthorities =
                new ArrayList<>();

        if ( user.getActive() ) {

            for ( Role role : user.getRoles() ) {

                simpleGrantedAuthorities.add( new SimpleGrantedAuthority(
                        role.getName() ) );
            }
        }

        return simpleGrantedAuthorities;
    }


    private UserDetails createUserDetails( User user ) {

        UserDetails userDetails =
                new UserDetails() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {

                        return createAuthorities( user );
                    }


                    @Override
                    public String getPassword() {

                        return user.getPasswordHash();
                    }


                    @Override
                    public String getUsername() {

                        return user.getEmail();
                    }


                    @Override
                    public boolean isAccountNonExpired() {

                        return true;
                    }


                    @Override
                    public boolean isAccountNonLocked() {

                        return true;
                    }


                    @Override
                    public boolean isCredentialsNonExpired() {

                        return true;
                    }


                    @Override
                    public boolean isEnabled() {

                        return user.getActive();
                    }
                };

        return userDetails;
    }


    @Bean
    public UserDetailsService userDetailsService() {

        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername( String username )
                    throws UsernameNotFoundException {

                logger.info( "username: " + username );

                Optional<User> optionalUser = repository.findByEmail( username );

                if ( optionalUser.isPresent() ) {

                    UserDetails userDetails = createUserDetails( optionalUser.get() );
                    return userDetails;
                }
                else {
                    throw new UsernameNotFoundException( "User with " + username + " not found" );
                }
            }
        };
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService( userDetailsService() );
        authProvider.setPasswordEncoder( passwordEncoder() );
        return authProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager( AuthenticationConfiguration config )
            throws Exception {

        return config.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
