package com.richard.earthquake.domain.services;

import com.richard.earthquake.data.model.User;
import com.richard.earthquake.data.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class UserService {
    @Autowired
    UsersRepo usersRepo;

    Flux<User> findAllUsers() {
        return usersRepo.findAll();
    }
}
