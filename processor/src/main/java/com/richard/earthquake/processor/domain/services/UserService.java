package com.richard.earthquake.processor.domain.services;

import com.richard.earthquake.processor.data.model.User;
import com.richard.earthquake.processor.data.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    @Autowired
    UsersRepo usersRepo;

    Flux<User> findAllUsers() {
        return usersRepo.findAll();
    }

    public Mono<String> removeTokenFromAllUsers(String token) {
        return findAllUsers().flatMap(user -> removeTokenFromUser(user, token)).then(Mono.just(token));
    }

    private Mono<User> removeTokenFromUser(User user, String token) {
        if (!user.getTokens().contains(token))
            return Mono.just(user);
        user.getTokens().remove(token);
        return usersRepo.save(user);
    }
}
