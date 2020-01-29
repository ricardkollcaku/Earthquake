package com.richard.earthquake.processor.domain.services;

import com.richard.earthquake.processor.data.model.Country;
import com.richard.earthquake.processor.data.repo.CountryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Service
public class CountryService {
    @Autowired
    CountryRepo countryRepo;
    Set<String> countries;

    @Autowired
    EarthquakeService earthquakeService;

    @PostConstruct
    void initCountries() {
        countries = new HashSet<>();
        countryRepo.count()
                .filter(aLong -> aLong == 0)
                .flatMapMany(aLong -> countryRepo.saveAll(getDistinctCountries()))
                .switchIfEmpty(addAllCountriesInSet())
                .subscribe()
        ;

    }

    private Flux<Country> addAllCountriesInSet() {
        return countryRepo.findAll().map(this::addCountriesInSet);
    }

    private Flux<Country> getDistinctCountries() {
        return earthquakeService.findDistinctByCountry()
                .map(this::createCountryObject)
                .map(this::addCountriesInSet)
                ;
    }

    private Country createCountryObject(String s) {
        //Todo;
        Country country = new Country(s, s);
        return country;
    }

    private Country addCountriesInSet(Country country) {
        countries.add(country.getCountry());
        System.out.println(countries.size());
        return country;
    }
}
