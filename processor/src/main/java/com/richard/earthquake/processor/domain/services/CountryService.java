package com.richard.earthquake.processor.domain.services;

import com.richard.earthquake.processor.data.model.Country;
import com.richard.earthquake.processor.data.repo.CountryRepo;
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
                .flatMapMany(aLong -> countryRepo.saveAll(getDistinctCountries()))
                .subscribe();

    }

    private Flux<Country> getDistinctCountries() {
        return earthquakeService.findDistinctByCountry()
                .map(Country::new);
    }


}
