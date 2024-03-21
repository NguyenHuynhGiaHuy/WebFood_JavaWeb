package com.spring.oshaneat.utils;

import com.spring.oshaneat.security.CustomUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtilsHelper {

    @Value("${jwt.privateKey}")
    private String privateKey;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    public String generateToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + 86400000);

        UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        String token = Jwts.builder()
                .subject(username)
                .claim("roles", role)
                .issuedAt( new Date())
                .expiration(expireDate)
                .signWith(key)
                .compact();

        return token;
    }

    public boolean verifyToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromJWT(String token){
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
}
