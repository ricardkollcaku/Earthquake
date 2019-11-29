package com.richard.earthquake.processor.domain.services;

import com.richard.earthquake.processor.data.repo.EarthquakeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class EarthquakeService {
    @Autowired
    StreamProvider streamProvider;
    @Autowired
    EarthquakeRepo earthquakeRepo;

    @PostConstruct
    void insertEarthquakes() {
        earthquakeRepo.saveAll(streamProvider.getStream())
                .doOnError(Throwable::printStackTrace)
                .retry()
                .subscribe();
    }
}
