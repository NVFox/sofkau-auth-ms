package com.sofkau.auth.application.services;

import com.sofkau.auth.application.exceptions.NotFoundException;
import com.sofkau.auth.application.ports.output.TokenProvider;
import com.sofkau.auth.application.repositories.TokenRepository;
import com.sofkau.auth.domain.entities.Customer;
import com.sofkau.auth.domain.entities.Token;
import com.sofkau.auth.domain.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private TokenServiceImpl tokenService;

    private final String testToken = "testToken";

    @Test
    void getValidToken_ShouldReturnOptionalToken() {
        when(tokenRepository.findLatestValidToken(anyLong()))
                .thenReturn(Optional.of(new Token()));

        Optional<Token> token = tokenService.getValidToken(1L);

        assertTrue(token.isPresent());
    }

    @Test
    void getToken_WhenTokenExists_ShouldReturnToken() {
        when(tokenRepository.findByToken(any()))
                .thenReturn(Optional.of(new Token()));

        Token token = assertDoesNotThrow(() -> tokenService
                .getToken(testToken));

        assertNotNull(token);
    }

    @Test
    void getToken_WhenTokenNotExists_ShouldThrowException() {
        when(tokenRepository.findByToken(any()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> tokenService
                .getToken(testToken));
    }

    @Test
    void revokeToken_ShouldRevokeToken() {
        Token token = new Token();

        tokenService.revokeToken(token);

        assertTrue(token.isRevoked());

        verify(tokenRepository, times(1))
                .save(token);
    }

    @Test
    void createToken_ShouldReturnCreatedToken() {
        Map<String, Object> claims = Map.of(
                "iat", System.currentTimeMillis() / 1000,
                "exp", System.currentTimeMillis() / 1000 + 60
        );

        when(customerService.getCustomer(anyLong()))
                .thenReturn(new Customer());

        when(tokenProvider.generateToken(anyString()))
                .thenReturn(testToken);

        when(tokenProvider.extractClaims(anyString()))
                .thenReturn(claims);

        when(tokenRepository.save(any()))
                .then(invocation -> invocation
                        .getArgument(0));

        Token token = assertDoesNotThrow(() -> tokenService
                .createToken(1L));

        assertNotNull(token);
        assertEquals(testToken, token.getAccessToken());
        assertEquals(Duration.ofMinutes(1), Duration.between(token.getIssuedAt(), token.getExpiresAt()));
    }
}
