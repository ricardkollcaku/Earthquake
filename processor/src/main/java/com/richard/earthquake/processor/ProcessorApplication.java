package com.richard.earthquake.processor;

import com.richard.earthquake.processor.data.repo.CountryRepo;
import com.richard.earthquake.processor.data.repo.EarthquakeRepo;
import com.richard.earthquake.processor.domain.services.ApiService;
import com.richard.earthquake.processor.domain.services.EarthquakeService;
import com.richard.earthquake.processor.domain.services.StreamProvider;
import com.richard.earthquake.processor.domain.util.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

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
    @Autowired
    CountryRepo countryRepo;

    public static void main(String[] args) {
        SpringApplication.run(ProcessorApplication.class, args);
    }

    /*
        db.earthquake.createIndex(
        { modifiedTime: 1 } ,
        { name: "earthquakeIndex" }
    )*/
    @Override
    public void run(String... args) throws Exception {
        countryRepo.findAll()
                .filter(country -> country.getCountryCode() == null);
        //     .subscribe(country -> System.out.println(country.getCountry()));




    }


}