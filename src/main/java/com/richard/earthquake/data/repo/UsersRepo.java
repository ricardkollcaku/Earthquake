package com.richard.earthquake.data.repo;

import com.richard.earthquake.data.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UsersRepo extends ReactiveMongoRepository<User, String> {
}
