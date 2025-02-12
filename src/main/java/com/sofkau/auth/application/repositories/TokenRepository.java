package com.sofkau.auth.application.repositories;

import com.sofkau.auth.domain.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t FROM Token t WHERE t.authToken = ?1")
    Optional<Token> findByToken(String token);
}
