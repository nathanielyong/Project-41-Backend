package com.snowtheghost.project41.services;

import com.snowtheghost.project41.infrastructure.authentication.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthenticationService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String extractToken(String bearerToken) {
        return bearerToken.split("\\s+")[1];
    }

    public String getUserId(String bearerToken) {
        return jwtTokenProvider.getUserId(extractToken(bearerToken));
    }

    public String generateToken(String userId) {
        return jwtTokenProvider.generateToken(userId);
    }
}
