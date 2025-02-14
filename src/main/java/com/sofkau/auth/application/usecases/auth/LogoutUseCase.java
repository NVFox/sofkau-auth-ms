package com.sofkau.auth.application.usecases.auth;

import com.sofkau.auth.application.ports.input.auth.Logout;
import com.sofkau.auth.domain.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutUseCase implements Logout {
    private final TokenService tokenService;

    @Override
    public void logout(long id) {
        tokenService.getValidToken(id)
                .ifPresent(tokenService::revokeToken);
    }
}
