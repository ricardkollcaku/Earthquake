package com.richard.earthquake.processor.data.repo;

import com.richard.earthquake.processor.data.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UsersRepo extends ReactiveMongoRepository<User, String> {
}
