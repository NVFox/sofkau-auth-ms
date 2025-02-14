package com.sofkau.auth.domain.services;

import com.sofkau.auth.domain.entities.Token;

import java.util.Optional;

public interface TokenService {
    Optional<Token> getValidToken(long customerId);

    Token getToken(String token);

    Token createToken(long customerId);

    void revokeToken(Token token);
}
