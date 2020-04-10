package com.richard.earthquake.app.presantation.controller.v1.authorized;

import com.richard.earthquake.app.data.dto.ChangePasswordDto;
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
    @Autowired
    ErrorUtil<TokenDto> tokenErrorUtil;


    @PostMapping("/register")
    public Mono<ResponseEntity<TokenDto>> create(@RequestBody User user) {
        return userService.registerAutoLogin(user)
                .map(MyObjectMapper::map)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).header(ErrorMessage.ERROR, ErrorMessage.USER_USER_EXIST).build()))
                .onErrorResume(throwable -> tokenErrorUtil.getResponseEntityAsMono(throwable));


    }

    @PostMapping("/login")
    public Mono<ResponseEntity<TokenDto>> login(@RequestBody LoginDto user) {
        return userService.login(user.getEmail(), user.getPassword())
                .map(MyObjectMapper::map)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).header(ErrorMessage.ERROR, ErrorMessage.USER_INCORRECT_AUTH_DATA).build()))
                .onErrorResume(throwable -> tokenErrorUtil.getResponseEntityAsMono(throwable));


    }

    @PutMapping("/token/{token}")
    public Mono<ResponseEntity<UserDto>> setToken(@PathVariable String token, ServerWebExchange serverHttpRequest) {
        return userService.initToken(MyObjectMapper.getUserId(serverHttpRequest), token)
                .map(MyObjectMapper::map)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).header(ErrorMessage.ERROR, ErrorMessage.USER_TOKEN_EXIST).build()))
                .onErrorResume(throwable -> userErrorUtil.getResponseEntityAsMono(throwable));
    }

    @PostMapping("/forgotPassword/{email}")
    public Mono<ResponseEntity<String>> forgotPassword(@PathVariable String email) {
        return userService.forgotPassword(email)
                .map(MyObjectMapper::map)
                .map(userDto -> ErrorMessage.FORGOT_PASSWORD_EMAIL_SEND)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).header(ErrorMessage.ERROR, ErrorMessage.USER_USER_NOT_EXIST).build()));
    }

    @PutMapping("/changePassword")
    public Mono<ResponseEntity<UserDto>> changePassword(@RequestBody ChangePasswordDto changePasswordDto, ServerWebExchange serverHttpRequest) {
        return userService.changePassword(MyObjectMapper.getUserId(serverHttpRequest), changePasswordDto)
                .map(MyObjectMapper::map)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).header(ErrorMessage.ERROR, ErrorMessage.USER_PASSWORD_NOT_MACH).build()));
    }


    @GetMapping("")
    public Mono<ResponseEntity<List<UserDto>>> findAllUsers() {
        return userService.findAll()
                .map(MyObjectMapper::map)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @PutMapping("/setNotification/{notification}")
    public Mono<ResponseEntity<UserDto>> setNotification(@PathVariable Boolean notification, ServerWebExchange serverHttpRequest) {
        return userService.setNotification(MyObjectMapper.getUserId(serverHttpRequest), notification)
                .map(MyObjectMapper::map)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).header(ErrorMessage.ERROR, ErrorMessage.USER_USER_NOT_EXIST_OR_AUTH_ERROR).build()))
                .onErrorResume(throwable -> userErrorUtil.getResponseEntityAsMono(throwable));
    }


    @PutMapping("/setSearchInFullDb/{inFullDb}")
    public Mono<ResponseEntity<UserDto>> setSearchInFullDb(@PathVariable Boolean inFullDb, ServerWebExchange serverHttpRequest) {
        return userService.setSearchInFullDb(MyObjectMapper.getUserId(serverHttpRequest), inFullDb)
                .map(MyObjectMapper::map)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).header(ErrorMessage.ERROR, ErrorMessage.USER_USER_NOT_EXIST_OR_AUTH_ERROR).build()))
                .onErrorResume(throwable -> userErrorUtil.getResponseEntityAsMono(throwable));
    }





    @GetMapping("/currentUser")
    public Mono<ResponseEntity<UserDto>> getCurrentUser(ServerWebExchange serverHttpRequest) {
        return userService.findUser(MyObjectMapper.getUserId(serverHttpRequest))
                .map(MyObjectMapper::map)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).header(ErrorMessage.ERROR, ErrorMessage.USER_USER_NOT_EXIST_OR_AUTH_ERROR).build()));

    }

}
