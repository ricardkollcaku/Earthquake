package com.richard.earthquake.processor.domain.services;

import com.richard.earthquake.processor.data.model.Earthquake;
import com.richard.earthquake.processor.data.model.Notification;
import com.richard.earthquake.processor.data.model.User;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Service
public class NotificationService {
    @Autowired
    UserService userService;

    @Autowired
    StreamProvider streamProvider;
    @Autowired
    PushNotificationService pushNotificationService;
    private static long lastEarthquake = 0;

    @PostConstruct
    void sendNotifications() {
        streamProvider.getStream()
                .filter(earthquake -> earthquake.getProperties().getTime() > lastEarthquake || earthquake.getProperties().getUpdated() > lastEarthquake)
                .map(this::updateLastEarthquake)
                .flatMap(earthquake -> filterBuyUser(earthquake)
                        .map(user -> sendNotificationsToUser(earthquake, user)));


    }

    private Earthquake sendNotificationsToUser(Earthquake earthquake, User user) {
        pushNotificationService.sendNotification(new Notification(earthquake, user));
        return earthquake;
    }

    private Flux<User> filterBuyUser(Earthquake earthquake) {
        return userService.findAllUsers()
                .flatMap(user -> Flux.fromIterable(user.getFilters())
                        .filter(filter -> filter.getMinMagnitude() < earthquake.getProperties().getMag() && filter.getGeometry().contains(earthquake.getGeometry()))
                        .collectList()
                        .filter(filters -> filters.size() > 0)
                        .map(filters -> user));
    }

    private Earthquake updateLastEarthquake(Earthquake earthquake) {
        lastEarthquake = (earthquake.getProperties().getUpdated() > 0) ? earthquake.getProperties().getUpdated() : earthquake.getProperties().getTime();
        return earthquake;
    }
}
