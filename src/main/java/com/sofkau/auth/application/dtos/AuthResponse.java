package com.sofkau.auth.application.dtos;

public record AuthResponse(
        String token,
        String email,
        long userId,
        String userName,
        String documentNumber
) {
}
