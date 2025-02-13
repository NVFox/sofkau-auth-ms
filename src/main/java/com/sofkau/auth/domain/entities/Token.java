package com.sofkau.auth.domain.entities;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Token {
    private Customer customer;

    private String accessToken;

    private LocalDateTime issuedAt;

    private LocalDateTime expiresAt;

    @Builder.Default
    private boolean revoked = false;

    public boolean isExpired() {
        return !LocalDateTime.now().isBefore(expiresAt);
    }
}
