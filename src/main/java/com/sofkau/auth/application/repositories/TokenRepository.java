package com.sofkau.auth.application.repositories;

import com.sofkau.auth.domain.entities.Token;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository {
    Optional<Token> findByToken(String token);

    Optional<Token> findLatestValidToken(long customerId);

    Token save(Token token);
}
