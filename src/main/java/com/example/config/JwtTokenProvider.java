package com.example.config;

import com.example.dto.ConnectionDto;
import com.example.model.Connection;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.config.SecurityConstants.EXPIRATION_TIME;
import static com.example.config.SecurityConstants.SECRET_KEY;

@Component
public class JwtTokenProvider {

    public String generateToken(Connection user) {
        Date now = new Date(System.currentTimeMillis());

        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        Map<String, Object> claims = new HashMap<>();
        setClaims(claims, user, null);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String generateTokenForTwitter(ConnectionDto user) {
        Date now = new Date(System.currentTimeMillis());

        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        Map<String, Object> claims = new HashMap<>();

        setClaims(claims, null, user);
        claims.put("screenName", user.getScreenName());
        claims.put("tokenSecret", user.getTokenSecret());

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch(SignatureException e) {
            System.out.println("Invalid JWT Signature");
        } catch(MalformedJwtException e) {
            System.out.println("Invalid JWT Token");
        } catch(ExpiredJwtException e) {
            System.out.println("Expired JWT token");
        } catch(UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token");
        } catch(IllegalArgumentException e) {
            System.out.println("JWT claims string is empty");
        }
        return false;
    }

    public Claims getClaimsFromJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return claims;
    }

    private void setClaims(Map<String, Object> claims, Connection user, ConnectionDto connectionDto) {
        if(user == null) {
            user = (ConnectionDto) connectionDto;
        }
        claims.put("userID", user.getUserID());
        claims.put("email", user.getEmail());
        claims.put("name", user.getName());
        claims.put("accessToken", user.getAccessToken());
        claims.put("pictureUrl", user.getPictureUrl());
        claims.put("providerId", user.getProviderId());
        claims.put("createdAt", user.getCreatedAt());
        claims.put("loggedAt", user.getLoggedAt());
    }
}
