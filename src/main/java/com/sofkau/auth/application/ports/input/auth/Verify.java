package com.sofkau.auth.application.ports.input.auth;

import com.sofkau.auth.application.dtos.AuthResponse;

public interface Verify {
    AuthResponse verify(String token);
}
