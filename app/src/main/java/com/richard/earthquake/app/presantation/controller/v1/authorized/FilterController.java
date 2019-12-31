package com.richard.earthquake.app.presantation.controller.v1.authorized;

import com.richard.earthquake.app.data.model.Filter;
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
@RequestMapping("/api/v1/filters")
public class FilterController {
    @Autowired
    FilterService filterService;
    @Autowired
    ErrorUtil<List<Filter>> errorUtil;

    @GetMapping("")
    public Mono<ResponseEntity<List<Filter>>> getAllFilters(ServerWebExchange serverWebExchange) {
        return filterService.findAllFilters(MyObjectMapper.getUserId(serverWebExchange))
                .collectList()
                .map(ResponseEntity::ok)
                .onErrorResume(throwable -> errorUtil.getResponseEntityAsMono(throwable));
    }

    @PostMapping("")
    public Mono<ResponseEntity<List<Filter>>> saveFilter(ServerWebExchange serverWebExchange, @RequestBody Filter filter) {
        return filterService.saveFilter(MyObjectMapper.getUserId(serverWebExchange), filter)
                .collectList()
                .map(ResponseEntity::ok)
                .onErrorResume(throwable -> errorUtil.getResponseEntityAsMono(throwable));
    }

    @DeleteMapping("")
    public Mono<ResponseEntity<Filter>> removeFilter(ServerWebExchange serverWebExchange, @RequestBody Filter filter) {
        return filterService.removeFilter(MyObjectMapper.getUserId(serverWebExchange), filter)
                .map(ResponseEntity::ok);

    }
}
