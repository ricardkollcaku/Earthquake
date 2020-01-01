package com.richard.earthquake.processor.domain.services;

import com.richard.earthquake.processor.data.model.FirebaseNotification;
import com.richard.earthquake.processor.data.model.Notification;
import com.richard.earthquake.processor.data.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Service
public class PushNotificationService {

    @Value("${firebase.security.authorization.key}")
    private String KEY;
    private EmitterProcessor<Notification> emitterProcessor;

    @PostConstruct
    private void runProcessor() {
        emitterProcessor = EmitterProcessor.create();
        emitterProcessor.flatMap(this::sendNotifications)

                .doOnError(Throwable::printStackTrace)
                .retry()
                .subscribe();
    }

    public void sendNotification(Notification notification) {
        emitterProcessor.onNext(notification);
    }

    private Flux<String> sendNotifications(Notification notification) {
        return getUserDeviceIds(notification.getUser())
                .flatMap(s -> pushNotification(new FirebaseNotification(notification.getData(), s)));
    }

    private Flux<String> getUserDeviceIds(User user) {
        return Flux.just();
    }


    private Mono<String> pushNotification(FirebaseNotification firebaseNotification) {
        System.out.println("per send");
        return WebClient.create("https://fcm.googleapis.com/fcm/send")
                .post()
                .header("Authorization", "key=" + KEY)
                .syncBody(firebaseNotification)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(String.class));
    }


}
