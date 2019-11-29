package com.richard.earthquake.processor;

import com.richard.earthquake.processor.domain.services.ApiService;
import com.richard.earthquake.processor.domain.services.EarthquakeService;
import com.richard.earthquake.processor.domain.services.StreamProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProcessorApplication implements CommandLineRunner {
    @Autowired
    StreamProvider streamProvider;
    @Autowired
    ApiService apiService;
    @Autowired
    EarthquakeService earthquakeService;

    public static void main(String[] args) {
        SpringApplication.run(ProcessorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        streamProvider.getStream().subscribe(System.out::println);

    }
}
