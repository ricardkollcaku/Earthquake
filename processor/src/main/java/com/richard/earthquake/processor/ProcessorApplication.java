package com.richard.earthquake.processor;

import com.richard.earthquake.processor.data.model.Earthquake;
import com.richard.earthquake.processor.data.repo.EarthquakeRepo;
import com.richard.earthquake.processor.domain.services.ApiService;
import com.richard.earthquake.processor.domain.services.EarthquakeService;
import com.richard.earthquake.processor.domain.services.StreamProvider;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.time.Duration;

@SpringBootApplication
public class ProcessorApplication implements CommandLineRunner {
    @Autowired
    StreamProvider streamProvider;
    @Autowired
    ApiService apiService;
    @Autowired
    EarthquakeService earthquakeService;
    @Autowired
    EarthquakeRepo earthquakeRepo;

    public static void main(String[] args) {
        SpringApplication.run(ProcessorApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        Flux.range(0, 240)
                .map(this::getByMonthRemoving);
               // .subscribe(System.out::println);
    }

    private String getByMonthRemoving(Integer integer) {
        DateTime dateTime = DateTime.now(DateTimeZone.UTC);
        String currentTime, previewTime;
        currentTime = dateTime.minusMonths(integer).toString();
        previewTime = dateTime.minusMonths(integer+1).toString();
        return currentTime + "  " + previewTime;
    }
}