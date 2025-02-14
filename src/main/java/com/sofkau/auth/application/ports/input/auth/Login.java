package com.sofkau.auth.application.ports.input.auth;

import com.sofkau.auth.application.dtos.AuthLoginRequest;
import com.sofkau.auth.application.dtos.AuthResponse;

public interface Login {
    AuthResponse login(AuthLoginRequest authLoginRequest);
}
