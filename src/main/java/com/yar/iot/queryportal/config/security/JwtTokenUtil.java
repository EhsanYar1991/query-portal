package com.yar.iot.queryportal.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * JWT token utils for
 * */
@Component
public class JwtTokenUtil {

    /**
     * The token expration duration
     * */
    @Value("${jwt.token.expiration.duration-minutes:30}")
    private Integer tokenExpirationMinutes;

    /**
     * The secret key for token encryption
     * */
    @Value("${jwt.secret:changeme}")
    private String secret;

    /**
     * Get username from the given token
     *
     * @param token The token
     * @return The username
     * */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Get expiration date from the given token
     *
     * @param token The token
     * @return The expiration date
     * */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Get a token claim
     *
     * @param token The token
     * @return The token claims
     * */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Get all token claims
     *
     * @param token The token
     * @return The username
     * */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * Validate the token expiration
     *
     * @param token The token
     * @return The expration validation
     * */
    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Generate the token
     *
     * @param authentication The authentication
     * @return Token
     * */
    public String generateToken(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims).setSubject(authentication.getName()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationMinutes * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * Validate the token expiration
     *
     * @param token The token
     * @param username The username
     * @return The token validation if username is matched and token hasn not expired.
     * */
    public boolean validateToken(String token, String username) {
        final String usernameFromToken = getUsernameFromToken(token);
        return usernameFromToken.equals(username) && !isTokenExpired(token);
    }
}
