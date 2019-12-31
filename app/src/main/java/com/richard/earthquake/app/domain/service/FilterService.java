package com.richard.earthquake.app.domain.service;

import com.richard.earthquake.app.data.model.Filter;
import com.richard.earthquake.app.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class FilterService {

    @Autowired
    UserService userService;

    public Flux<Filter> findAllFilters(Mono<String> userId) {
        return userService.findUser(userId)
                .flatMapIterable(User::getFilters);
    }

    public Flux<Filter> saveFilter(Mono<String> userId, Filter filter) {
        return userService.findUser(userId)
                .flatMap(user -> updateOrAddUserFilter(user, filter))
                .flatMap(user -> userService.save(user))
                .flatMapIterable(User::getFilters);

    }

    private Mono<User> updateOrAddUserFilter(User user, Filter filter) {
        return Flux.fromIterable(user.getFilters())
                .filter(filter1 -> !filter1.getName().equals(filter.getName()))
                .collectList()
                .map(filters -> addFilterToList(filters, filter))
                .map(filters -> addFiltersToUser(filters, user));
    }

    private User addFiltersToUser(List<Filter> filters, User user) {
        user.setFilters(filters);
        return user;
    }

    private List<Filter> addFilterToList(List<Filter> filters, Filter filter) {
        filters.add(filter);
        return filters;
    }
}
