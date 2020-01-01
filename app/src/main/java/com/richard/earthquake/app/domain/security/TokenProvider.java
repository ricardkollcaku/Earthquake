package com.richard.earthquake.app.domain.security;

import com.richard.earthquake.app.data.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
public class TokenProvider {


    private byte[] key;
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;


    public Mono<String> resolveToken(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION))
                .filter(s -> s.length() > 0 && s.startsWith("Bearer "))
                .map(s -> s.substring(7));
    }

    @PostConstruct
    protected void init() {
        key = Base64.getEncoder().encode(secretKey.getBytes());

    }

    public String createToken(User user) {

        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("lastName", user.getLastName());
        claims.put("email", user.getEmail());
        claims.put("auth", Stream.of(
                new SimpleGrantedAuthority(
                        user.getSystemRole()
                ))
                .collect(toList()));
        Date now = new Date();


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(Keys.hmacShaKeyFor(key), SignatureAlgorithm.HS256)
                .compact();

    }


    public String getEmail(String token) {
        try {
            return token != null ? Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject() : null;
        } catch (ExpiredJwtException e) {
            return (String) e.getClaims().get("sub");
        }
    }



    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
