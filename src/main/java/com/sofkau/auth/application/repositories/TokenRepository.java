package com.sofkau.auth.application.repositories;

import com.sofkau.auth.domain.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t FROM Token t WHERE t.accessToken = ?1 AND t.revoked IS FALSE")
    Optional<Token> findByToken(String token);

    @Query("SELECT t FROM Token t WHERE t.customer.id = ?1 AND t.revoked IS FALSE " +
            "AND CURRENT_TIMESTAMP < t.expiresAt ORDER BY t.issuedAt DESC LIMIT 1")
    Optional<Token> findLatestValidToken(long customerId);
}
