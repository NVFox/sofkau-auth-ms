package com.sofkau.auth.application.usecases.auth;

import com.sofkau.auth.application.dtos.AuthLoginRequest;
import com.sofkau.auth.application.dtos.AuthResponse;
import com.sofkau.auth.application.ports.input.auth.Login;
import com.sofkau.auth.application.ports.output.EventPublisher;
import com.sofkau.auth.domain.entities.Customer;
import com.sofkau.auth.domain.entities.Token;
import com.sofkau.auth.domain.events.customer.CustomerAuthenticatedEvent;
import com.sofkau.auth.domain.services.CustomerService;
import com.sofkau.auth.domain.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase implements Login {
    private final TokenService tokenService;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;

    @Override
    public AuthResponse login(AuthLoginRequest authLoginRequest) {
        Customer customer = customerService
                .getCustomerByEmail(authLoginRequest.email());

        if (!passwordEncoder.matches(authLoginRequest.password(), customer.getPassword()))
            throw new BadCredentialsException("Wrong password");

        Token token = tokenService.createOrGetToken(customer.getId());

        eventPublisher
                .publish(new CustomerAuthenticatedEvent(customer.getId(), token.getAccessToken()));

        return new AuthResponse(
                token.getAccessToken(),
                customer.getEmail(),
                customer.getId(),
                customer.getName(),
                customer.getDocumentNumber()
        );
    }
}
