package com.sofkau.auth.application.services;

import com.sofkau.auth.application.exceptions.NotFoundException;
import com.sofkau.auth.application.ports.output.TokenProvider;
import com.sofkau.auth.application.repositories.TokenRepository;
import com.sofkau.auth.domain.entities.Token;
import com.sofkau.auth.domain.services.CustomerService;
import com.sofkau.auth.domain.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import static com.sofkau.auth.constants.TokenMessageConstants.TOKEN_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final CustomerService customerService;

    @Override
    public Token createOrGetToken(long customerId) {
        return tokenRepository.findLatestValidToken(customerId)
                .orElse(createToken(customerId));
    }

    @Override
    public Token getToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException(TOKEN_NOT_FOUND));
    }

    @Override
    public void revokeToken(Token token) {
        token.setRevoked(true);
        tokenRepository.save(token);
    }

    private Token createToken(long customerId) {
        String accessToken = tokenProvider
                .generateToken(String.valueOf(customerId));

        Map<String, Object> claims = tokenProvider
                .extractClaims(accessToken);

        LocalDateTime issuedAt = convertToLocalDateTime((long) claims.get("iat"));
        LocalDateTime expiresAt = convertToLocalDateTime((long) claims.get("exp"));

        Token token = Token.builder()
                .accessToken(accessToken)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .customer(customerService.getCustomer(customerId))
                .build();

        return tokenRepository.save(token);
    }

    private LocalDateTime convertToLocalDateTime(long seconds) {
        return Instant.ofEpochSecond(seconds)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
