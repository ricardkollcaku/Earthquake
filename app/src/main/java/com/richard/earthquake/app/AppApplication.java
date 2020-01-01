package com.richard.earthquake.app;

import com.richard.earthquake.app.data.model.Earthquake;
import com.richard.earthquake.app.data.model.Properties;
import com.richard.earthquake.app.data.repo.EarthquakeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@SpringBootApplication

public class AppApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }
@Autowired
    EarthquakeRepo earthquakeRepo;
    @Override
    public void run(String... args) throws Exception {

    }
}
