package com.sofkau.auth.application.usecases.auth;

import com.sofkau.auth.domain.entities.Token;
import com.sofkau.auth.domain.services.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutUseCaseTest {
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private LogoutUseCase logoutUseCase;

    @Test
    void logout_WhenTokenExists_ShouldRevokeToken() {
        long customerId = 1L;

        when(tokenService.getValidToken(customerId))
                .thenReturn(Optional.of(new Token()));

        logoutUseCase.logout(customerId);

        verify(tokenService, times(1))
                .revokeToken(any(Token.class));
    }
}
