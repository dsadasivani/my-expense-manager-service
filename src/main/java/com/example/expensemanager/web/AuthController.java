package com.example.expensemanager.web;

import com.example.expensemanager.dto.AuthRefreshRequest;
import com.example.expensemanager.dto.AuthRefreshResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${app.security.jwt.secret}") private String secret;
    @Value("${app.security.jwt.issuer}") private String issuer;
    @Value("${app.security.jwt.accessMinutes:60}") private int accessMinutes;

    @PostMapping("/refresh")
    public ResponseEntity<AuthRefreshResponse> refresh(@RequestBody AuthRefreshRequest req){
        // NOTE: Demo-only — normally you'd validate the refresh token against a store.
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        Instant now = Instant.now();
        String token = Jwts.builder()
                .setIssuer(issuer)
                .setSubject("demo-user")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(accessMinutes * 60L)))
                .signWith(key, io.jsonwebtoken.SignatureAlgorithm.HS256) // <- specify alg for 0.11.x
                .compact();
        // Echo the provided refreshToken back (or rotate if you implement a store)
        return ResponseEntity.ok(new AuthRefreshResponse(token, req.refreshToken()));
    }
}
