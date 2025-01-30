package org.manmet.crmapplication.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.manmet.crmapplication.model.Permissions;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    // Generate a secure key programmatically
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final long expirationMs = 1000 * 60 * 60; // 1git hours

    /**
     * Generate a JWT token for a given username.
     *
     * @param username the username for which to generate the token.
     * @param roles the roles associated with the user.
     * @param permissions the permissions associated with the user.
     * @return the generated JWT token.
     */
    public String generateToken(String username, Set<String> roles, Set<Permissions> permissions) {
        List<String> permissionNames = permissions.stream()
                .map(Permissions::getPermissionName)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .claim("permissions", permissionNames)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey) // Use the secure key
                .compact();
    }

    /**
     * Extract claims from a JWT token.
     *
     * @param token the JWT token.
     * @return the claims contained in the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // Use the secure key
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extract a specific claim from a JWT token.
     *
     * @param token the JWT token.
     * @param claimsResolver a function to resolve the claim.
     * @param <T> the type of the claim.
     * @return the claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract the username from a JWT token.
     *
     * @param token the JWT token.
     * @return the username extracted from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract the expiration date from a JWT token.
     *
     * @param token the JWT token.
     * @return the expiration date extracted from the token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Check if the JWT token is expired.
     *
     * @param token the JWT token.
     * @return true if the token is expired, false otherwise.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validate the JWT token.
     *
     * @param token the JWT token.
     * @param userDetails the user details to validate against.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

