package com.sofkau.auth.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateCustomerRequest(
        String name,
        @Email String email,
        String documentNumber,
        @Size(min = 6) String password
) {
}
