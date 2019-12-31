package com.richard.earthquake.app.domain.service;

import com.richard.earthquake.app.data.model.Earthquake;
import com.richard.earthquake.app.data.repo.EarthquakeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EarthquakeService {
    @Autowired
    UserService userService;
    @Autowired
    EarthquakeRepo earthquakeRepo;

    public Flux<Earthquake> findAllEarthquakes(Mono<String> userId) {
        userService
                .findUser(userId)


    }

}
