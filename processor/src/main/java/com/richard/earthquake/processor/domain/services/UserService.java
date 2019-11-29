package com.richard.earthquake.processor.domain.services;

import com.richard.earthquake.processor.data.model.User;
import com.richard.earthquake.processor.data.repo.UsersRepo;
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
