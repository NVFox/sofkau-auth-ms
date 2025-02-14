package com.sofkau.auth.infrastructure.persistence;

import com.sofkau.auth.application.repositories.TokenRepository;
import com.sofkau.auth.domain.entities.Token;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TokenCacheRepository implements TokenRepository {
    private final Cache tokenCache;
    private final Cache sessionCache;

    public TokenCacheRepository(@Qualifier("tokenCache") Cache tokenCache,
                                @Qualifier("sessionCache") Cache sessionCache) {
        this.tokenCache = tokenCache;
        this.sessionCache = sessionCache;
    }

    @Override
    public Optional<Token> findByToken(String token) {
        try {
            return Optional.ofNullable(tokenCache.get(token, Token.class))
                    .filter(t -> !t.isRevoked());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Token> findLatestValidToken(long customerId) {
        try {
            return Optional.ofNullable(sessionCache.get(customerId, Token.class))
                    .filter(t -> !t.isRevoked());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Token save(Token token) {
        tokenCache.put(token.getAccessToken(), token);
        sessionCache.put(token.getCustomer().getId(), token);
        return token;
    }
}
