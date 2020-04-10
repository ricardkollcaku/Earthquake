package com.richard.earthquake.processor.domain.services;

import com.richard.earthquake.processor.data.model.Earthquake;
import com.richard.earthquake.processor.data.model.Notification;
import com.richard.earthquake.processor.data.model.NotificationData;
import com.richard.earthquake.processor.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

@Service
public class NotificationService {
    @Autowired
    UserService userService;
    @Autowired
    StreamProvider streamProvider;
    @Autowired
    PushNotificationService pushNotificationService;

    @PostConstruct
    void sendNotifications() {
        streamProvider.getStream()
                .flatMap(earthquake -> filterBuyUser(earthquake)
                        .map(user -> sendNotificationsToUser(earthquake, user)))
                .subscribe();
    }

    private Earthquake sendNotificationsToUser(Earthquake earthquake, User user) {
        NotificationData notificationData = CreateNotificationData(earthquake);
        pushNotificationService.sendNotification(new Notification(notificationData, user));
        return earthquake;
    }

    private NotificationData CreateNotificationData(Earthquake earthquake) {
        NotificationData notificationData = new NotificationData();
        notificationData.setTitle("Earthquake "+earthquake.getCountry()+" "+earthquake.getMag());
        notificationData.setMag(earthquake.getMag());
        notificationData.setDescription(earthquake.getProperties().getTitle());
        return notificationData;
    }

    private Flux<User> filterBuyUser(Earthquake earthquake) {
        return userService.findAllUsers()
                .filter(User::getIsNotificationEnabled)
                .flatMap(user -> Flux.fromIterable(user.getFilters())
                        .filter(filter ->filter.getIsNotificationEnabled() &&filter.getMinMagnitude() <= earthquake.getProperties().getMag() && filter.getCountryKey().equals(earthquake.getCountryKey()))
                        .collectList()
                        .filter(filters -> filters.size() > 0)
                        .map(filters -> user));
    }


}
