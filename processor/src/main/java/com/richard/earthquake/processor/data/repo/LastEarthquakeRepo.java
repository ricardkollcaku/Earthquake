package com.richard.earthquake.processor.data.repo;

import com.richard.earthquake.processor.data.model.LastEarthquakes;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface LastEarthquakeRepo extends ReactiveMongoRepository<LastEarthquakes, String> {
    Mono<Void> deleteAllByTimeLessThan(Long time);
}
