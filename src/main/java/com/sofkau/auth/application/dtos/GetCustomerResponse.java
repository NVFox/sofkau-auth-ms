package com.sofkau.auth.application.dtos;

import lombok.Builder;

@Builder
public record GetCustomerResponse(
        String name,
        String email,
        String documentNumber,
        String password
) {
}
