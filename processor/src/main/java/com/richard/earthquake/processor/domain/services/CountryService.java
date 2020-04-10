package com.richard.earthquake.processor.domain.services;

import com.richard.earthquake.processor.data.repo.CountryRepo;
import com.richard.earthquake.processor.domain.util.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

@Service
public class CountryService {
    @Autowired
    CountryRepo countryRepo;

    @Autowired
    EarthquakeService earthquakeService;

    @PostConstruct
    void initCountries() {
        countryRepo.count()
                .filter(aLong -> aLong == 0)
                .flatMapMany(aLong -> countryRepo.saveAll(Flux.fromIterable(ObjectMapper.getCountries())))
                .subscribe();

    }

}
