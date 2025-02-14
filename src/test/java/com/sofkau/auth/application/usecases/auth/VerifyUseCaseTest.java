package com.sofkau.auth.application.usecases.auth;

import com.sofkau.auth.application.dtos.AuthResponse;
import com.sofkau.auth.application.exceptions.token.NotValidTokenFoundException;
import com.sofkau.auth.domain.entities.Customer;
import com.sofkau.auth.domain.entities.Token;
import com.sofkau.auth.domain.services.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VerifyUseCaseTest {
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private VerifyUseCase verifyUseCase;

    @Test
    void verify_WhenTokenIsValid_ThenReturnAuthResponse() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("name")
                .email("email")
                .documentNumber("documentNumber")
                .password("password")
                .build();

        Token token = Token.builder()
                .accessToken("token")
                .customer(customer)
                .build();

        when(tokenService.getToken("token"))
                .thenReturn(token);

        AuthResponse response = verifyUseCase.verify("token");

        assertEquals("token", response.token());
        assertEquals("email", response.email());
        assertEquals("name", response.userName());
        assertEquals(1L, response.userId());
        assertEquals("documentNumber", response.documentNumber());
    }

    @Test
    void verify_WhenTokenIsNotValid_ThenThrowException() {
        when(tokenService.getToken(anyString()))
                .thenThrow(new RuntimeException());

        assertThrows(NotValidTokenFoundException.class, () -> verifyUseCase
                .verify("token"));
    }
}
