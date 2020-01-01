package com.richard.earthquake.app.domain.service;

import com.richard.earthquake.app.data.model.DummyError;
import com.richard.earthquake.app.presantation.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ErrorUtil<T> {

    public Mono<T> performDummyError(DummyError dummyError) {
        return Mono.error(dummyError);
    }

    private DummyError getDummyError(Throwable throwable) {
        if (throwable instanceof DummyError)
            return (DummyError) throwable;
        else return new DummyError(throwable);
    }

    private ResponseEntity<T> getResponseEntity(Throwable throwable) {
        return ResponseEntity.status(getDummyError(throwable).getHttpStatus()).header(ErrorMessage.ERROR, getDummyError(throwable).getError()).build();
    }

    public Mono<ResponseEntity<T>> getResponseEntityAsMono(Throwable throwable) {
        return Mono.just(getResponseEntity(throwable));
    }

}
