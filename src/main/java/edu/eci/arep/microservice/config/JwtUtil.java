package edu.eci.arep.microservice.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;
    private SecretKey key;
    // Initializes the key after the class is instantiated and the jwtSecret is injected, 
    // preventing the repeated creation of the key and enhancing performance
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    // Generate JWT token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    // Get username from JWT token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    // Validate JWT token
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.info("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.info("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.info("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.info("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.info("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }
}