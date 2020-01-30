package com.richard.earthquake.processor.data.repo;

import com.richard.earthquake.processor.data.model.Earthquake;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface EarthquakeRepo extends ReactiveMongoRepository<Earthquake, String> {
    Mono<Earthquake> findTopByOrderByModifiedTimeDesc();
}
