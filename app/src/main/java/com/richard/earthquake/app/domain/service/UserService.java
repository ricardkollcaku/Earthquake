package com.richard.earthquake.app.domain.service;

import com.richard.earthquake.app.data.model.DummyError;
import com.richard.earthquake.app.data.model.User;
import com.richard.earthquake.app.data.repo.UserRepo;
import com.richard.earthquake.app.presantation.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashSet;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    ErrorUtil<User> errorUtil;

    public Mono<User> createUser(User user) {
        if (user.getFilters() == null)
            user.setFilters(new ArrayList<>());
        if (user.getTokens() == null)
            user.setTokens(new HashSet<>());
        return userRepo.findById(user.getEmail())
                .flatMap(user1 -> errorUtil.performDummyError(new DummyError(ErrorMessage.USER_USER_EXIST, user.getEmail(), HttpStatus.CONFLICT)))
                .switchIfEmpty(save(user));
    }

    public Mono<User> save(User user) {
        return userRepo.save(user);
    }

    public Mono<User> initToken(Mono<String> userId, String token) {
        return findUser(userId)
                .flatMap(user1 -> setTokenToUser(user1, token))
                .flatMap(this::save);
    }

    private Mono<User> setTokenToUser(User user, String token) {
        if (user.getTokens().contains(token))
            return Mono.empty();
        user.getTokens().add(token);
        return Mono.just(user);
    }

    public Mono<User> findUser(Mono<String> userId) {
        return userRepo.findById(userId);
    }

    public Flux<User> findAll() {
        return userRepo.findAll();
    }
}
