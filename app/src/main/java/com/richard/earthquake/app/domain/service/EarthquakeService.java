package com.richard.earthquake.app.domain.service;

import com.richard.earthquake.app.data.model.Earthquake;
import com.richard.earthquake.app.data.model.Filter;
import com.richard.earthquake.app.data.model.User;
import com.richard.earthquake.app.data.repo.EarthquakeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
                .flatMapIterable(User::getFilters)
                .map(filter -> getEarthquakeCriteria(filter))
                .collectList()
                .map(criteria -> getFiltersQuery(criteria, pageable))
                .flatMapMany(query -> reactiveMongoTemplate.find(query, Earthquake.class));
    }

    private Query getFiltersQuery(List<Criteria> criteria, Pageable pageable) {
        Criteria[] criterias = new Criteria[criteria.size()];
        criteria.toArray(criterias);
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(criterias));
        query.with(pageable);
        System.out.println(query.getQueryObject().toString());
        return query;
    }

    private Criteria getEarthquakeCriteria(Filter filter) {
        return Criteria.where("geometry").within(filter.getGeometry()).andOperator(Criteria.where("properties.mag").gte(filter.getMinMagnitude()));

    }


}
