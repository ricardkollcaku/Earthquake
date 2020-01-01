package com.richard.earthquake.app.presantation.controller.v1.authorized;

import com.richard.earthquake.app.data.dto.LoginDto;
import com.richard.earthquake.app.data.dto.TokenDto;
import com.richard.earthquake.app.data.dto.UserDto;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    ErrorUtil<UserDto> userErrorUtil;

    @PostMapping("/register")
    public Mono<ResponseEntity<UserDto>> create(@RequestBody User user) {
        return userService.createUser(user)
                .map(MyObjectMapper::map)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).header(ErrorMessage.ERROR, ErrorMessage.USER_USER_EXIST).build()))
                .onErrorResume(throwable -> userErrorUtil.getResponseEntityAsMono(throwable));


    }

    @PostMapping("/login")
    public Mono<ResponseEntity<TokenDto>> login(@RequestBody LoginDto user) {
        return userService.login(user.getEmail(), user.getPassword())
                .map(MyObjectMapper::map)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).header(ErrorMessage.ERROR, ErrorMessage.USER_INCORRECT_AUTH_DATA).build()));


    }

    @PutMapping("/token/{token}")
    public Mono<ResponseEntity<UserDto>> setToken(@PathVariable String token, ServerWebExchange serverHttpRequest) {
        return userService.initToken(MyObjectMapper.getUserId(serverHttpRequest), token)
                .map(MyObjectMapper::map)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).header(ErrorMessage.ERROR, ErrorMessage.USER_TOKEN_EXIST).build()))
                .onErrorResume(throwable -> userErrorUtil.getResponseEntityAsMono(throwable));
    }


    @GetMapping("")
    public Mono<ResponseEntity<List<UserDto>>> findAllUsers() {
        return userService.findAll()
                .map(MyObjectMapper::map)
                .collectList()
                .map(ResponseEntity::ok);
    }


}
