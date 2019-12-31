package com.richard.earthquake.app.domain.service;

import com.richard.earthquake.app.data.model.User;
import com.richard.earthquake.app.data.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public Mono<User> createUser(User user) {
        return this.save(user);
    }

    public Mono<User> save(User user) {
        return userRepo.save(user);
    }

    public Mono<User> initToken(Mono<String> userId, String token) {
        return  findUser(userId)
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

}
