package com.sofkau.auth.application.dtos;

import lombok.Builder;

@Builder
public record GetCustomerResponse(
        long id,
        String name,
        String email,
        String documentNumber,
        String password
) {
}
