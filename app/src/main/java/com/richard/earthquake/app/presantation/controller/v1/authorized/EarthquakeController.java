package com.richard.earthquake.app.presantation.controller.v1.authorized;

import com.richard.earthquake.app.data.model.Earthquake;
import org.springframework.data.mongodb.core.geo.GeoJson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/earthquake")
public class EarthquakeController {

    @GetMapping
    public Mono<ResponseEntity<Earthquake>> findAllEarthquakes(){

    }

}
