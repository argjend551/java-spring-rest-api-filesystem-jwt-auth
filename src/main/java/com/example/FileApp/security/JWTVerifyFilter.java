package com.example.FileApp.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.FileApp.services.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTVerifyFilter extends OncePerRequestFilter {

    private final UserService userService;

    public JWTVerifyFilter(UserService userService) {
        this.userService = userService;
    }

    

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException
    {

        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        var jwtToken = authorizationHeader.substring("Bearer ".length());

        if (jwtToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            var algorithm = Algorithm.HMAC256("secret".getBytes());
            var verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();

            DecodedJWT jwt = verifier.verify(jwtToken);

            var user = userService.loadUserByUsername(jwt.getSubject());

            var authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    user.getPassword(),
                    user.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (JWTVerificationException exception){
            throw new IllegalStateException("Failed to authenticate");
        }

    }

}
