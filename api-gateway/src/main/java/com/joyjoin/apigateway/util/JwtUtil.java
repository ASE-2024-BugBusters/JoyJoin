package com.joyjoin.apigateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;


@Service
public class JwtUtil {
    public static final String SECRET_KEY = "9abd22e669dcceb6feb83a9999a53f6328934b177790c9dbf219bdc3114389465b31470ef9d929ff82fb0c829321d74ac630a3d938a66274e16162576da10f06";

    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
