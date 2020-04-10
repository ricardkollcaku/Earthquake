package com.richard.earthquake.processor.domain.services;

import com.richard.earthquake.processor.data.model.FirebaseNotification;
import com.richard.earthquake.processor.data.model.Notification;
import com.richard.earthquake.processor.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Service
public class PushNotificationService {
@Autowired
UserService userService;
    @Value("${firebase.security.authorization.key}")
    private String KEY;
    private DirectProcessor<Notification> emitterProcessor;

    @PostConstruct
    private void runProcessor() {
        emitterProcessor = DirectProcessor.create();
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
        return Flux.fromIterable(user.getTokens());
    }


    private Mono<String> pushNotification(FirebaseNotification firebaseNotification) {
        System.out.println("per send"+ firebaseNotification.toString());
        return WebClient.create("https://fcm.googleapis.com/fcm/send")
                .post()
                .header("Authorization", "key=" + KEY)
                .bodyValue(firebaseNotification)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(String.class))
                .flatMap(s->checkIfError(s,firebaseNotification.getTo()))
                .log();
    }

    private Mono<String> checkIfError(String s,String token) {
        if (!s.contains("NotRegistered"))
            return Mono.just(s);
       return userService.removeTokenFromAllUsers(token);
    }


}
