package com.raizesdonordeste.api.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret:minha-chave-secreta-super-segura-raizes-do-nordeste-123456}")
    private String secret;

    @SuppressWarnings("java:S2143") // Converte Instant -> Date exigido pela interface da biblioteca JJWT
    public String gerarToken(String email) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(email)
                .issuer("api-raizes-do-nordeste")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(gerarDataExpiracao()))
                .signWith(key)
                .compact();
    }

    public String getSubject(String tokenJWT) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(tokenJWT)
                .getPayload()
                .getSubject();
    }

    private Instant gerarDataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
