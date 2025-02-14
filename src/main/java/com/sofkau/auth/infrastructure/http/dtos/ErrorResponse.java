package com.sofkau.auth.infrastructure.http.dtos;

public record ErrorResponse(
        String error,
        String message
) {
}
