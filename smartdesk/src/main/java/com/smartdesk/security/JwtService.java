
package com.smartdesk.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long jwtExpirationMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long jwtExpirationMs) {

        byte[] keyBytes = Decoders.BASE64.decode(secret);

        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.jwtExpirationMs = jwtExpirationMs;
    }

    // Generate a JWT for the authenticated user
    public String generateToken(UserDetails userDetails) {

        Date issuedAt = new Date();
        Date expiration = new Date(
                issuedAt.getTime() + jwtExpirationMs
        );

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(signingKey)
                .compact();
    }

    // Extract the email (subject) from the JWT
    public String extractUsername(String token) {

        return extractAllClaims(token).getSubject();
    }

    // Validate the token against the user
    public boolean isTokenValid(
            String token,
            UserDetails userDetails) {

        try {
            Claims claims = extractAllClaims(token);

            String username = claims.getSubject();

            return username.equals(userDetails.getUsername())
                    && claims.getExpiration().after(new Date());

        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    // Extract all claims after verifying the token signature
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}


//The JwtAuthenticationFilter will:
//1. Extract the token from the header.
//2. Call jwtService.extractUsername(token).
//3. Receive alice@gmail.com.
//4. Ask CustomUserDetailsService to load Alice from the database.
//5. Receive Alice's UserDetails object.
//6. Call jwtService.isTokenValid(token, userDetails).
//7. If valid, establish her authenticated identity in Spring Security.
//That's how the pieces fit together.