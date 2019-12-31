package com.richard.earthquake.app.presantation.controller.v1.authorized;

import com.richard.earthquake.app.data.model.User;
import com.richard.earthquake.app.domain.service.ErrorUtil;
import com.richard.earthquake.app.domain.service.UserService;
import com.richard.earthquake.app.presantation.ErrorMessage;
import com.richard.earthquake.app.presantation.MyObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    ErrorUtil<User> userErrorUtil;

    @PostMapping("")
    public Mono<ResponseEntity<User>> create(@RequestBody User user) {
        return userService.createUser(user)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).header(ErrorMessage.ERROR, ErrorMessage.USER_USER_EXIST).build()));


    }

    @PutMapping("/token//{token}")
    public Mono<ResponseEntity<User>> setToken(@PathVariable String token, ServerWebExchange serverHttpRequest) {
        return userService.initToken(MyObjectMapper.getUserId(serverHttpRequest), token)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).header(ErrorMessage.ERROR, ErrorMessage.USER_USER_EXIST).build()))
                .onErrorResume(throwable -> userErrorUtil.getResponseEntityAsMono(throwable));
    }


}
