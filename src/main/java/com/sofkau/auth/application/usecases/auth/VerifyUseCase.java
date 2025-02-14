package com.sofkau.auth.application.usecases.auth;

import com.sofkau.auth.application.dtos.AuthResponse;
import com.sofkau.auth.application.exceptions.token.NotValidTokenFoundException;
import com.sofkau.auth.application.ports.input.auth.Verify;
import com.sofkau.auth.domain.entities.Token;
import com.sofkau.auth.domain.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyUseCase implements Verify {
    private final TokenService tokenService;

    @Override
    public AuthResponse verify(String token) {
        try {
            Token tk = tokenService.getToken(token);

            return new AuthResponse(
                    tk.getAccessToken(),
                    tk.getCustomer().getEmail(),
                    tk.getCustomer().getId(),
                    tk.getCustomer().getName(),
                    tk.getCustomer().getDocumentNumber()
            );
        } catch (Exception e) {
            throw new NotValidTokenFoundException();
        }
    }
}
