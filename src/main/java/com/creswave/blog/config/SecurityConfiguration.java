package com.creswave.blog.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.creswave.blog.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity( prePostEnabled = true )
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception {

        http.cors( Customizer.withDefaults() );
        http

                .csrf(
                        csrf -> csrf.disable() )

                .authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers(
                                    "/api/v1/login/authenticate",
                                    "/api/v1/register/save",
                                    "/error" )
                                .permitAll()
                                .requestMatchers( "/api/v1/*" ).authenticated()
                                .anyRequest()
                                .authenticated() )
                .sessionManagement(
                        sessionManagement -> sessionManagement
                                .sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS ) )

                .authenticationProvider( authenticationProvider )

                .addFilterBefore( jwtAuthFilter, UsernamePasswordAuthenticationFilter.class )

                .logout(
                        logout -> logout
                                .logoutUrl( "/api/v1/auth/logout" )
                                .addLogoutHandler( logoutHandler )
                                .logoutSuccessHandler(
                                        ( request, response,
                                                authentication ) -> SecurityContextHolder
                                                        .clearContext() ) );

        return http.build();

    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins( Arrays.asList( "*" ) );

        configuration.setAllowedMethods(
                Arrays.asList( "GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS" ) );

        configuration.setExposedHeaders( List.of( "X-Error-Type" ) );

        configuration.setAllowedHeaders( List.of( "*" ) );
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration( "/**", configuration );
        return source;
    }
}
