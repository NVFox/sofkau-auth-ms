package com.sofkau.auth.infrastructure.http.controllers;

import com.sofkau.auth.application.dtos.AuthLoginRequest;
import com.sofkau.auth.application.dtos.AuthResponse;
import com.sofkau.auth.application.dtos.VerifyTokenRequest;
import com.sofkau.auth.application.ports.input.auth.Login;
import com.sofkau.auth.application.ports.input.auth.Logout;
import com.sofkau.auth.application.ports.input.auth.Verify;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final Login login;
    private final Logout logout;
    private final Verify verify;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid AuthLoginRequest authLoginRequest) {
        return login.login(authLoginRequest);
    }

    @PostMapping("/logout/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@PathVariable long id) {
        logout.logout(id);
    }

    @PostMapping("/verify")
    public AuthResponse verify(@RequestBody @Valid VerifyTokenRequest token) {
        return verify.verify(token.token());
    }
}
