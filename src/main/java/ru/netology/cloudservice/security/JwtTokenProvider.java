package ru.netology.cloudservice.security;

import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import ru.netology.cloudservice.model.entity.UserEntity;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public Boolean validateToken(String token, UserEntity userEntity) {
        String username = getUsernameFromToken(token);
        return (username != null && !isTokenExpired(token) && username.equals(userEntity.getName()));
    }

    public String generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        long expirationMillies = TimeUnit.MINUTES.toMillis(expirationTime);
        return Jwts.builder()
                .claims(claims)
                .subject(user.getName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMillies))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String resolveToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        SecretKey secretBytes = Keys.hmacShaKeyFor(secretKey.getBytes());
        Claims claims = Jwts.parser()
                .verifyWith(secretBytes)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims;
    }

    private Boolean isTokenExpired(String token) {
        return getClaim(token, Claims::getExpiration).before(new Date());
    }
}
