package com.richard.earthquake.app.domain.service;

import com.richard.earthquake.app.data.model.Country;
import com.richard.earthquake.app.data.repo.CountryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class CountryService {
    @Autowired
    private CountryRepo countryRepo;

    public Flux<Country> findAllCountries(){
        return countryRepo.findAll();
    }
}
