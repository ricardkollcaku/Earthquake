package com.richard.earthquake.processor.data.repo;

import com.richard.earthquake.processor.data.model.Country;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CountryRepo extends ReactiveMongoRepository<Country, String> {
}
