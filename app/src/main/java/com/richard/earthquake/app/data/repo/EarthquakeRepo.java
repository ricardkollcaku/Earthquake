package com.richard.earthquake.app.data.repo;

import com.richard.earthquake.app.data.model.Earthquake;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface EarthquakeRepo extends ReactiveMongoRepository<Earthquake, String> {
}
