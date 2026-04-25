package com.leonardo.delivery_tracking_system.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.leonardo.delivery_tracking_system.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    @Value("${spring.security.jwt.secret-key}")
    private String secret;

    @Value("${spring.security.jwt.expiration:2}")
    private long expirationHours;

    private static final String ISSUER = "delivery_tracking_system";

    public String generateToken(User user){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plus(expirationHours, ChronoUnit.HOURS))
                .sign(algorithm);
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException e){
            return "";
        }
    }
}
