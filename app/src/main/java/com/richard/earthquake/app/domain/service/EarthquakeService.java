package com.richard.earthquake.app.domain.service;

import com.richard.earthquake.app.data.model.Earthquake;
import com.richard.earthquake.app.data.model.Filter;
import com.richard.earthquake.app.data.model.LastEarthquakes;
import com.richard.earthquake.app.data.model.User;
import com.richard.earthquake.app.data.repo.EarthquakeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class EarthquakeService {
    @Autowired
    UserService userService;
    @Autowired
    EarthquakeRepo earthquakeRepo;
    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    public Flux<Earthquake> getAllUserEarthquakesPageable(Mono<String> userId, Pageable pageable) {
        return userService.findUser(userId)
                .flatMapMany(user ->
                       Flux.just(user)
                        .flatMapIterable(User::getFilters)
                        .map(this::getEarthquakeCriteria).collectList()
                        .flatMap(criteria -> getFiltersQuery(criteria, pageable))
                        .flatMapMany(query -> user.isFullDatabaseSearch()?reactiveMongoTemplate.find(query, Earthquake.class):reactiveMongoTemplate.find(query, LastEarthquakes.class))
                        .switchIfEmpty(getAllFilteredEarthquake(pageable, null, null,user.isFullDatabaseSearch())))
                ;
    }

    private Mono<Query> getFiltersQuery(List<Criteria> criteria, Pageable pageable) {
        if (criteria.size() == 0)
            return Mono.empty();
        Criteria[] criterias = new Criteria[criteria.size()];
        criteria.toArray(criterias);
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(criterias));
        query.with(pageable);
        query.with(Sort.by(new Sort.Order(Sort.Direction.DESC, "time")));
        return Mono.just(query);
    }

    private Criteria getEarthquakeCriteria(Filter filter) {

        return Criteria.where("countryKey").is(filter.getCountryKey())
                .andOperator(Criteria.where("mag").gte(filter.getMinMagnitude()));

    }

    public Flux<Earthquake> getAllFilteredEarthquake(Pageable of, Short countryKey, Double mag, Boolean fullDBSearch) {
        Criteria criteria = getCriteria(countryKey, mag);
        Query query = new Query();
        if (criteria != null)
            query.addCriteria(criteria);
        query.with(of);
        query.with(Sort.by(new Sort.Order(Sort.Direction.DESC, "time")));
        if (fullDBSearch)
        return reactiveMongoTemplate.find(query, Earthquake.class);
        else
        return reactiveMongoTemplate.find(query, LastEarthquakes.class)
                .map(lastEarthquakes -> lastEarthquakes);
    }

    private Criteria getCriteria(Short countryKey, Double mag) {
        if (countryKey == null && mag == null)
            return null;

        if (mag != null && countryKey == null)
            return Criteria.where("mag").gte(mag);

        if (countryKey != null && mag == null)
            return Criteria.where("countryKey").is(countryKey);
        return Criteria.where("mag").gte(mag).andOperator(Criteria.where("countryKey").is(countryKey));
    }


}
