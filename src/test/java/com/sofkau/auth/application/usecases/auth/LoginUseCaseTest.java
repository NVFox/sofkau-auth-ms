package com.sofkau.auth.application.usecases.auth;

import com.sofkau.auth.application.dtos.AuthLoginRequest;
import com.sofkau.auth.application.dtos.AuthResponse;
import com.sofkau.auth.application.ports.output.EventPublisher;
import com.sofkau.auth.domain.entities.Customer;
import com.sofkau.auth.domain.entities.Token;
import com.sofkau.auth.domain.events.customer.CustomerAuthenticatedEvent;
import com.sofkau.auth.domain.services.CustomerService;
import com.sofkau.auth.domain.services.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {
    @Mock
    private TokenService tokenService;

    @Mock
    private CustomerService customerService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private LoginUseCase loginUseCase;

    private final String testEmail = "test@mail.com";

    private final String testPassword = "testPassword";

    private final String testToken = "testToken";

    @Test
    void login_WhenEverythingIsOk_ShouldReturnToken() {
        Customer customer = Customer.builder()
                .id(1L)
                .email(testEmail)
                .password(testPassword)
                .build();

        Token token = Token.builder()
                .customer(customer)
                .accessToken(testToken)
                .build();

        AuthLoginRequest authLoginRequest = new AuthLoginRequest(
                testEmail,
                testPassword
        );

        when(customerService.getCustomerByEmail(anyString()))
                .thenReturn(customer);

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(true);

        when(tokenService.getValidToken(anyLong()))
                .thenReturn(Optional.of(token));

        AuthResponse result = assertDoesNotThrow(() -> loginUseCase
                .login(authLoginRequest));

        assertNotNull(result);
        assertEquals(testToken, result.token());

        verify(tokenService, never())
                .createToken(anyLong());

        verify(eventPublisher, times(1))
                .publish(any(CustomerAuthenticatedEvent.class));
    }

    @Test
    void login_WhenPasswordIsIncorrect_ShouldThrowException() {
        Customer customer = Customer.builder()
                .id(1L)
                .email(testEmail)
                .password(testPassword)
                .build();

        AuthLoginRequest authLoginRequest = new AuthLoginRequest(
                testEmail,
                testPassword
        );

        when(customerService.getCustomerByEmail(anyString()))
                .thenReturn(customer);

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> loginUseCase
                .login(authLoginRequest));

        verify(tokenService, never())
                .getValidToken(anyLong());

        verify(eventPublisher, times(1))
                .publish(any(CustomerAuthenticatedEvent.class));
    }

    @Test
    void login_WhenTokenIsNotValid_ShouldReturnNewToken() {
        Customer customer = Customer.builder()
                .id(1L)
                .email(testEmail)
                .password(testPassword)
                .build();

        Token token = Token.builder()
                .customer(customer)
                .accessToken(testToken)
                .build();

        AuthLoginRequest authLoginRequest = new AuthLoginRequest(
                testEmail,
                testPassword
        );

        when(customerService.getCustomerByEmail(anyString()))
                .thenReturn(customer);

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(true);

        when(tokenService.getValidToken(anyLong()))
                .thenReturn(Optional.empty());

        when(tokenService.createToken(anyLong()))
                .thenReturn(token);

        AuthResponse result = assertDoesNotThrow(() -> loginUseCase
                .login(authLoginRequest));

        assertNotNull(result);
        assertEquals(testToken, result.token());

        verify(tokenService, times(1))
                .createToken(anyLong());

        verify(eventPublisher, times(1))
                .publish(any(CustomerAuthenticatedEvent.class));
    }
}
