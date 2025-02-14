package com.sofkau.auth.application.dtos;

import jakarta.validation.constraints.NotBlank;

public record VerifyTokenRequest(
        @NotBlank String token
) {
}
