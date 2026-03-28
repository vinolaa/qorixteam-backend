package com.github.ovinola.qorixteam.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

/**
 * Serviço responsável por gerar e validar os tokens JWT.
 */
@Service
public class JwtService {

    // Em produção, isso DEVE vir do application.yml e ter no mínimo 256 bits (32 caracteres)
    private static final String SECRET = "QorixTeamSuperSecretKeyForJwtValidation2026!";
    private static final long EXPIRATION_TIME = 86400000; // 24 horas em milissegundos

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(UUID userId, String username) {
        return Jwts.builder()
            .subject(userId.toString()) // Guardamos o UUID no 'subject' (assunto) do token
            .claim("username", username) // Podemos guardar dados extras (claims)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(getSigningKey())
            .compact();
    }

    public UUID extractUserId(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
        return UUID.fromString(claims.getSubject());
    }

    public boolean isTokenValid(String token) {
        try {
            extractUserId(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}