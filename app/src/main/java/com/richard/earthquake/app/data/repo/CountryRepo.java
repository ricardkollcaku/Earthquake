package com.richard.earthquake.app.data.repo;

import com.richard.earthquake.app.data.model.Country;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CountryRepo extends ReactiveMongoRepository<Country, String> {
}
