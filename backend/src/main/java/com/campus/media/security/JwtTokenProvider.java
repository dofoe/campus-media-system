package com.campus.media.security;

import com.campus.media.config.JwtConfig;
import com.campus.media.entity.User;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    
    private final JwtConfig jwtConfig;
    
    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }
    
    public String generateToken(User user) {
        return Jwts.builder()
            .subject(user.getUsername())
            .claim("userId", user.getId())
            .claim("username", user.getUsername())
            .claim("name", user.getName())
            .claim("role", user.getRole().getCode())
            .claim("deptId", user.getDeptId())
            .claim("dataScope", user.getDataScope())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
            .signWith(jwtConfig.secretKey())
            .compact();
    }
}
