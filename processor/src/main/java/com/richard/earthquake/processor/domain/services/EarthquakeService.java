package com.richard.earthquake.processor.domain.services;

import com.richard.earthquake.processor.data.model.Earthquake;
import com.richard.earthquake.processor.data.repo.EarthquakeRepo;
import com.richard.earthquake.processor.data.repo.LastEarthquakeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Service
public class EarthquakeService {
    @Autowired
    private StreamProvider streamProvider;
    @Autowired
    private EarthquakeRepo earthquakeRepo;
    @Autowired
    private LastEarthquakeRepo lastEarthquakeRepo;
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @PostConstruct
    private void insertEarthquakes() {
        insertAllStreamEarthquakes();
        insertLastStreamEarthquakes();
    }

    private void insertAllStreamEarthquakes() {
        saveAll(streamProvider.getStream())
                .doOnError(Throwable::printStackTrace)
                .retry()
                .subscribe();
    }

    private void insertLastStreamEarthquakes() {
        saveAll(streamProvider.getStream())
                .filter(earthquake -> earthquake.getTime() > (System.currentTimeMillis() - 8640000000L))
                .doOnError(Throwable::printStackTrace)
                .retry()
                .subscribe();
    }

    public Mono<Earthquake> getLastEarthquake() {
        return earthquakeRepo.findTopByOrderByTimeDesc();
    }


    public Flux<Earthquake> saveAll(Flux<Earthquake> stream) {
        return earthquakeRepo.saveAll(stream);
    }

}
