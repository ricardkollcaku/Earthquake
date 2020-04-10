package com.richard.earthquake.app.presantation.controller.v1.authorized;

import com.richard.earthquake.app.data.model.Earthquake;
import com.richard.earthquake.app.domain.service.EarthquakeService;
import com.richard.earthquake.app.domain.service.ErrorUtil;
import com.richard.earthquake.app.presantation.ErrorMessage;
import com.richard.earthquake.app.presantation.MyObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/earthquake")
public class EarthquakeController {
    @Autowired
    EarthquakeService earthquakeService;
    @Autowired
    ErrorUtil<List<Earthquake>> errorUtil;

    @GetMapping("/user")
    public Mono<ResponseEntity<List<Earthquake>>> findAllEarthquakesForUser(ServerWebExchange serverHttpRequest, @RequestParam Integer pageNumber, @RequestParam Integer elementPerPage) {
        return earthquakeService.getAllUserEarthquakesPageable(MyObjectMapper.getUserId(serverHttpRequest), PageRequest.of(pageNumber, elementPerPage))
                .collectList()
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).header(ErrorMessage.ERROR, ErrorMessage.EARTHQUAKE_NO_EARTHQUAKES_FOR_USER).build()))
                ;
    }

    @GetMapping("/all")
    public Mono<ResponseEntity<List<Earthquake>>> findAllEarthquakesForUser(@RequestParam Integer pageNumber, @RequestParam Integer elementPerPage, @RequestParam(required = false) Short countryKey, @RequestParam(required = false) Integer mag) {
        return earthquakeService.getAllFilteredEarthquake(PageRequest.of(pageNumber, elementPerPage), countryKey, mag,false)
                .collectList()
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).header(ErrorMessage.ERROR, ErrorMessage.EARTHQUAKE_NO_EARTHQUAKES_FOR_USER).build()))
                ;
    }


}
