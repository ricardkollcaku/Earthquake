package com.richard.earthquake.data.repo;

import com.richard.earthquake.data.model.Earthquake;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface EarthquakeRepo extends ReactiveMongoRepository<Earthquake, String> {
}
