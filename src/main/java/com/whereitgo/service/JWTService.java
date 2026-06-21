package com.whereitgo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secret;

    // KEY CREATION
    private SecretKey getKey() {
        log.info("Generating signing key from secret");

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        log.debug("Key generated successfully");
        return key;
    }

    // CREATE TOKEN
    public String generateToken(String userId) {

        log.info("Generating JWT token for userId: {}", userId);

        String token = Jwts.builder()
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 3))
                .signWith(getKey())
                .compact();

        log.debug("Token generated successfully: {}", token);

        return token;
    }

    // EXTRACT USER ID
    public String extractUserId(String token) {

        log.info("Extracting userId from token");

        Claims claims = extractAllClaims(token);

        String userId = claims.getSubject();

        log.debug("Extracted userId: {}", userId);

        return userId;
    }

    // PARSE CLAIMS
    private Claims extractAllClaims(String token) {

        log.info("Parsing JWT token");

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            log.debug("Token parsed successfully. Claims: {}", claims);

            return claims;

        } catch (Exception e) {
            log.error("Failed to parse JWT token: {}", e.getMessage());
            throw e;
        }
    }

    // VALIDATION
    public boolean isTokenValid(String token) {

        log.info("Validating JWT token");

        try {
            boolean expired = extractAllClaims(token).getExpiration().before(new Date());

            log.debug("Token expired status: {}", expired);

            return !expired;

        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
}