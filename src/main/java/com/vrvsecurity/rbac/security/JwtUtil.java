package com.vrvsecurity.rbac.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for handling JWT operations such as token creation, validation, and claims extraction.
 * <p>
 * This class provides methods to generate JWTs, validate them, and extract claims like username and expiration.
 * It uses the `io.jsonwebtoken` library for JWT operations.
 * </p>
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret; // Secret key for signing JWTs

    @Value("${jwt.expiration}")
    private Long expiration; // Token expiration duration in seconds

    /**
     * Retrieves the signing key for JWT operations.
     *
     * @return The signing key derived from the secret.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT for the given user.
     *
     * @param userDetails The details of the user for whom the token is generated.
     * @return A JWT as a string.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Creates a JWT with the specified claims and subject (username).
     *
     * @param claims  The additional claims to include in the token.
     * @param subject The subject of the token (typically the username).
     * @return A JWT as a string.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username from the given JWT.
     *
     * @param token The JWT from which to extract the username.
     * @return The username (subject) contained in the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the given JWT.
     *
     * @param token The JWT from which to extract the expiration date.
     * @return The expiration date of the token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT.
     *
     * @param token          The JWT from which to extract the claim.
     * @param claimsResolver A function to extract the desired claim.
     * @param <T>            The type of the claim to extract.
     * @return The extracted claim.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT.
     *
     * @param token The JWT from which to extract claims.
     * @return A {@link Claims} object containing all claims in the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Validates the given JWT against the user's details.
     *
     * @param token       The JWT to validate.
     * @param userDetails The details of the user to validate against.
     * @return True if the token is valid, false otherwise.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if the given token is expired.
     *
     * @param token The JWT to check.
     * @return True if the token is expired, false otherwise.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
