package com.richard.earthquake;

import com.richard.earthquake.domain.services.ApiService;
import com.richard.earthquake.domain.services.EarthquakeService;
import com.richard.earthquake.domain.services.StreamProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EarthquakeApplication implements CommandLineRunner {
    @Autowired
    StreamProvider streamProvider;
    @Autowired
    ApiService apiService;
    @Autowired
    EarthquakeService earthquakeService;

    public static void main(String[] args) {
        SpringApplication.run(EarthquakeApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        streamProvider.getStream().subscribe(System.out::println);

    }
}
