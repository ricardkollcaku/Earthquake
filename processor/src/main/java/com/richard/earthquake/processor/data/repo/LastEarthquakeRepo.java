package com.richard.earthquake.processor.data.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface LastEarthquakeRepo extends ReactiveMongoRepository<LastEarthquakeRepo, String> {
}
