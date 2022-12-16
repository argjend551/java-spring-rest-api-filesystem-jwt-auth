package com.example.FileApp.security;

import com.example.FileApp.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
   
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            UserService userService,
            AuthenticationManager authManager
    )

            throws Exception{
        return http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JWTLoginFilter(authManager))
                .addFilterAfter(new JWTVerifyFilter(userService), JWTLoginFilter.class)
                .authorizeRequests()
                .antMatchers("/api/v1/user/register")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .build();
    }

    
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(10);
    }


}



