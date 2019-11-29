package com.richard.earthquake.domain.services;

import com.richard.earthquake.data.model.Earthquake;
import org.springframework.stereotype.Component;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.function.Predicate;

@Component
public class StreamProvider {
    private DirectProcessor<Earthquake> emitterProcessor;


    public StreamProvider() {
        emitterProcessor = DirectProcessor.create();
        emitterProcessor.subscribe();
        emitterProcessor.doOnError(Throwable::printStackTrace);
        emitterProcessor.doOnComplete(() -> System.out.println("emmiter processor completed"));
    }

    public void subscribe(Flux<Earthquake> flux) {
        flux.doOnNext(t -> emitterProcessor.onNext(t))
                .doOnComplete(() -> {
                    System.out.println("Stream provider complete");
                    restartStream(flux);
                })
                .doOnError(Throwable::printStackTrace)
                .retry()
                .subscribe();
    }

    private void restartStream(Flux flux) {
        Mono.just(true)
                .delayElement(Duration.ofMillis(500))
                .map(aBoolean -> {
                    subscribe(flux);
                    return aBoolean;
                }).subscribe();
    }

    public Flux<Earthquake> getStream(Predicate<Earthquake> predicate) {
        return predicate != null ? emitterProcessor.filter(predicate) : emitterProcessor;
    }

    public Flux<Earthquake> getStream() {
        return emitterProcessor;
    }


}
