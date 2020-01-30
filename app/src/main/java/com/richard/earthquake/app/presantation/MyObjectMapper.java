package com.richard.earthquake.app.presantation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.richard.earthquake.app.data.dto.TokenDto;
import com.richard.earthquake.app.data.dto.UserDto;
import com.richard.earthquake.app.data.model.DummyError;
import com.richard.earthquake.app.data.model.User;
import com.richard.earthquake.app.domain.service.ErrorUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class MyObjectMapper {
    public static Mono<User> map(ObjectMapper objectMapper, ServerWebExchange serverWebExchange, ErrorUtil<User> errorUtil) {
        try {
            return serverWebExchange.getAttributes().get("userId") != null ? getUserFromRequest(serverWebExchange, objectMapper) : errorNotExistingUser(errorUtil);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return errorUtil.performDummyError(new DummyError(ErrorMessage.USER_ERROR_PARSING_USER, null, HttpStatus.CONFLICT));

        }

    }

    private static Mono<User> errorNotExistingUser(ErrorUtil<User> errorUtil) {
        return errorUtil.performDummyError(new DummyError(ErrorMessage.USER_USER_NOT_EXIST_OR_AUTH_ERROR, null, HttpStatus.NO_CONTENT));
    }


    private static Mono<User> getUserFromRequest(ServerWebExchange serverWebExchange, ObjectMapper objectMapper) throws JsonProcessingException {
        return Mono.just(objectMapper.readValue(serverWebExchange.getAttributes().get("user").toString(), User.class));
    }

    public static Mono<String> getUserId(ServerWebExchange serverWebExchange) {
        return serverWebExchange.getAttributes().get("userId") != null ? Mono.just(serverWebExchange.getAttributes().get("userId").toString()) : Mono.error(new DummyError(ErrorMessage.USER_USER_NOT_EXIST_OR_AUTH_ERROR, null, HttpStatus.NO_CONTENT));
        //  return Mono.just("richard_kollcaku@hotmail.com");
        //TODO per t fshi komentin
    }

    public static UserDto map(User user) {
        return new UserDto(user.getEmail(), user.getFirstName(), user.getLastName(), user.getIsNotificationEnabled());
    }

    public static TokenDto map(String s) {
        return new TokenDto(s);
    }
}
