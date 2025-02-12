package com.sofkau.auth.application.ports.output;

import java.util.Map;

public interface TokenProvider {
    String generateToken(String subject);

    Map<String, Object> extractClaims(String token);
}
