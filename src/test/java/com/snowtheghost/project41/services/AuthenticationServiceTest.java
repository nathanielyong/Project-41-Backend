package com.snowtheghost.project41.services;

import com.snowtheghost.project41.infrastructure.authentication.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExtractToken_Success() {
        String bearerToken = "Bearer token";

        String extractedToken = authenticationService.extractToken(bearerToken);

        assertEquals("token", extractedToken);
    }

    @Test
    void testGetUserId_Success() {
        String bearerToken = "Bearer token";
        String token = "token";
        String userId = "user-id";

        when(jwtTokenProvider.getUserId(token)).thenReturn(userId);

        String extractedUserId = authenticationService.getUserId(bearerToken);

        assertEquals(userId, extractedUserId);

        verify(jwtTokenProvider).getUserId(token);
        verifyNoMoreInteractions(jwtTokenProvider);
    }

    @Test
    void testGenerateToken_Success() {
        String userId = "user-id";
        String generatedToken = "generated-token";

        when(jwtTokenProvider.generateToken(userId)).thenReturn(generatedToken);

        String token = authenticationService.generateToken(userId);

        assertEquals(generatedToken, token);

        verify(jwtTokenProvider).generateToken(userId);
        verifyNoMoreInteractions(jwtTokenProvider);
    }
}
