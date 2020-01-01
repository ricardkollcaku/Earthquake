package com.richard.earthquake.app.data.repo;

import com.richard.earthquake.app.data.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepo extends ReactiveMongoRepository<User, String> {
}
