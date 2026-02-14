package com.elite.resort.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil
{
    // ✅ Plain String Secret Key (Keep it at least 32+ characters)
    private static final String SECRET_KEY =
            "resortbookingsecretkeyresortbookingsecretkey";

    // ✅ 1 Day Expiration
    private static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 24;

    // ✅ GENERATE TOKEN (email + id + role)
    public String generateToken(String email, String id, String role) {

        return Jwts.builder()
                .setSubject(email)            // Email
                .claim("id", id)              // userId or adminId
                .claim("role", role)          // USER / ADMIN
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME)
                )
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // ✅ EXTRACT ALL CLAIMS
    public Claims extractClaims(String token) {

        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ EXTRACT EMAIL
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // ✅ EXTRACT ID (User/Admin)
    public String extractId(String token) {
        return extractClaims(token).get("id", String.class);
    }

    // ✅ EXTRACT ROLE
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    // ✅ VALIDATE TOKEN
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
