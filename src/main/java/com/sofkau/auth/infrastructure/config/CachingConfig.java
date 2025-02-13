package com.sofkau.auth.infrastructure.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.sofkau.auth.domain.entities.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CachingConfig {
    public static final String TOKEN_CACHE = "tokens";

    public static final String SESSION_CACHE = "sessions";

    @Bean
    public Cache tokenCache(CacheManager cacheManager) {
        return cacheManager.getCache(TOKEN_CACHE);
    }

    @Bean
    public Cache sessionCache(CacheManager cacheManager) {
        return cacheManager.getCache(SESSION_CACHE);
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> tokenCaffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(TOKEN_CACHE, SESSION_CACHE);
        cacheManager.setCaffeine(tokenCaffeine);
        return cacheManager;
    }

    @Bean
    public Caffeine<Object, Object> tokenCaffeine(@Value("${security.jwt.expiration-time}") long expirationTime) {
        return Caffeine.newBuilder()
                .initialCapacity(10)
                .maximumSize(100)
                .expireAfter(tokenExpires(expirationTime));
    }

    private static Expiry<Object, Object> tokenExpires(long expirationTime) {
        return Expiry.creating((s, token) ->
                token instanceof Token t
                        ? Duration.between(t.getIssuedAt(), t.getExpiresAt())
                        : Duration.ofMillis(expirationTime));
    }
}
