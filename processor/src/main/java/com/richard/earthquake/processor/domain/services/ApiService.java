package com.richard.earthquake.processor.domain.services;

import com.richard.earthquake.processor.data.dto.EarthquakesDto;
import com.richard.earthquake.processor.data.model.Earthquake;
import com.richard.earthquake.processor.domain.util.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Service
public class ApiService {
    @Autowired
    StreamProvider streamProvider;

    @PostConstruct
    void getDataFromApi() {
        streamProvider.subscribe(
                /*       Flux.just(1)*/
                Flux.interval(Duration.ofSeconds(30))
                        .flatMap(aLong -> getRequest()));
        //https://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php;

    }

    private Flux<Earthquake> getRequest() {
        DateTime dateTime = DateTime.now(DateTimeZone.UTC);
        String currentTime, previewTime;
        currentTime = dateTime.minusSeconds(0).toString();
        previewTime = dateTime.minusHours(1).toString();

        return WebClient.create("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=" + previewTime + "&endtime=" + currentTime)
                .get()
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(EarthquakesDto.class))
                .flatMapIterable(EarthquakesDto::getEarthquakes)
                .map(ObjectMapper::map).log();
    }
}
