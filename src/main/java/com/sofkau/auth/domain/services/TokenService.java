package com.sofkau.auth.domain.services;

import com.sofkau.auth.domain.entities.Token;

public interface TokenService {
    Token createOrGetToken(long customerId);

    Token getToken(String token);

    void revokeToken(Token token);
}
