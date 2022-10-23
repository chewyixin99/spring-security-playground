package com.example.jwtdemo.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwtdemo.jwttoken.JwtTokenService;

@RestController
public class AuthController {
    
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final JwtTokenService jwtTokenService;

    public AuthController(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/token")
    public String token(Authentication authentication) {
        LOG.debug("Token requested for user: '{}", authentication.getName());
        String token = jwtTokenService.generateToken(authentication);
        LOG.debug("Token granted {}", token);
        return token;
    }

}
