package com.sofkau.auth.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateCustomerRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String documentNumber,
        @NotBlank @Size(min = 6) String password
) {
}
