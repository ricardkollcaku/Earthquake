package com.richard.earthquake.app.presantation.controller.v1.authorized;

import com.richard.earthquake.app.data.model.Country;
import com.richard.earthquake.app.data.model.Filter;
import com.richard.earthquake.app.domain.service.CountryService;
import com.richard.earthquake.app.domain.service.ErrorUtil;
import com.richard.earthquake.app.domain.service.FilterService;
import com.richard.earthquake.app.presantation.MyObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/country")
public class CountryController {
    @Autowired
    CountryService countryService;


    @GetMapping("")
    public Mono<ResponseEntity<List<Country>>> getAllFilters() {
        return countryService.findAllCountries()
                .collectList()
                .map(ResponseEntity::ok);
    }




}
