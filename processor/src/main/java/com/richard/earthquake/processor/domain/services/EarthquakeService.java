package com.richard.earthquake.processor.domain.services;

import com.richard.earthquake.processor.data.model.Earthquake;
import com.richard.earthquake.processor.data.repo.EarthquakeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

@Service
public class EarthquakeService {
    @Autowired
    private StreamProvider streamProvider;
    @Autowired
    private EarthquakeRepo earthquakeRepo;
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @PostConstruct
    private void insertEarthquakes() {
        earthquakeRepo.saveAll(streamProvider.getStream())
                .doOnError(Throwable::printStackTrace)
                .retry()
                .subscribe();
    }

    public Flux<String> findDistinctByCountry() {
        return reactiveMongoTemplate.findDistinct("country", Earthquake.class, String.class);
    }
}
