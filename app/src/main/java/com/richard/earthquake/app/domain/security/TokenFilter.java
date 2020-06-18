package com.richard.earthquake.app.domain.security;

import com.richard.earthquake.app.data.model.User;
import com.richard.earthquake.app.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
public class TokenFilter implements ServerAuthenticationConverter {
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    UserService userService;

    @Override
    public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange)
                .flatMap(serverWebExchange1 -> tokenProvider.resolveToken(serverWebExchange1))
                .filter(s -> tokenProvider.validateToken(s))
                .flatMap(s -> getAuthentication(s, serverWebExchange));
    }

    public Mono<Authentication> getAuthentication(String token, ServerWebExchange serverWebExchange) {
        return userService.findUser(Mono.just(tokenProvider.getEmail(token)))
                .map(user -> putUserIdInWebExchange(user, serverWebExchange))
                .flatMap(user ->
                        Mono.just(new UsernamePasswordAuthenticationToken(user, "", Stream
                                .of(new SimpleGrantedAuthority(user.getSystemRole()))
                                .collect(toList())))
                );

    }

    private User putUserIdInWebExchange(User user, ServerWebExchange serverWebExchange) {
        serverWebExchange.getAttributes().put("userId", user.getEmail());
        return user;
    }


}
