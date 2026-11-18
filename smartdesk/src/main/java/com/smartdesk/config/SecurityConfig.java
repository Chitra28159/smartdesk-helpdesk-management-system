
package com.smartdesk.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.smartdesk.security.JwtAuthenticationFilter;

import jakarta.servlet.DispatcherType;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider,
            JwtAuthenticationFilter jwtAuthenticationFilter)
            throws Exception {

    	  http
    	  .csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(auth -> auth
        		    .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
        		    .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
        		    .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
        		    .anyRequest().authenticated()
        		)
          .authenticationProvider(authenticationProvider)
          .addFilterBefore(
                  jwtAuthenticationFilter,
                  UsernamePasswordAuthenticationFilter.class
              )
          .exceptionHandling(exception -> exception
        		    .authenticationEntryPoint(authenticationEntryPoint())
        		)
          .sessionManagement(session -> session
        		    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        		)
         ;

//in the above code,the spring provides the basic set of filters as a chain in the builder class httpsecurity. we're adding the basicauthenticationfilter that takes basic object and checks the base64 username and password and provides an authentication 0object. the authorization checks for the authentication object for anyrequest it is and authorizes the request.
//auth is the AuthorizationManagerRequestMatcherRegistry object that provides the methods to authorize the requests based on the request matchers. we can use anyRequest() method to match all the requests and then use authenticated() method to check if the request is authenticated or not. if it is authenticated then it will authorize the user, i.e., allow the request to proceed otherwise it will return 401 unauthorized error.
//so auth is the whole registry. anyRequest().authenticated() is the entry you add to it. any request that matches the entry and is authenticated will be authorized.
//allows POST requests to /api/users without authentication, while requiring authentication for all other requests.
     // HttpSecurity builds the filter chain.
     // httpBasic -> adds BasicAuthenticationFilter: reads the Basic header, authenticates
     //                   via AuthenticationManager, stores the Authentication in SecurityContext.
     // authorizeHttpRequests -> 'auth' is the rule registry (AuthorizationManagerRequestMatcherRegistry).
     //   anyRequest().authenticated() is one rule: match every request, require a logged-in user.
     // At request time AuthorizationFilter checks the rules against the Authentication:
     //   passes -> request proceeds; fails -> 401 (not logged in) or 403 (not permitted).

        
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //The PasswordEncoder interface is used to perform one-way transformation of passwords for storage. The BCryptPasswordEncoder class is an implementation of the PasswordEncoder interface that uses the BCrypt hashing function to encode passwords. It is a good practice to use a strong hashing function like BCrypt to store passwords securely in the database.

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }
    
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
    }
    
    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
        
     //   //The AuthenticationManager is the main strategy interface for authentication. It has a single method, authenticate(), which authenticates the provided Authentication object. The AuthenticationManager is responsible for coordinating the authentication process, delegating to one or more AuthenticationProviders to perform the actual authentication.
    }


}
