package com.richard.earthquake.app.data.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class DummyError extends Throwable {
    private String error;
    private String id;
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public DummyError(String error, String id, HttpStatus httpStatus) {
        this.error = error;
        this.id = id;
        this.httpStatus = httpStatus;
    }

    public DummyError(Throwable throwable) {
        super(throwable);
    }

    public DummyError(String message, String error, String id, HttpStatus httpStatus) {
        super(message);
        this.error = error;
        this.id = id;
        this.httpStatus = httpStatus;
    }

    public DummyError(String message, Throwable cause, String error, String id, HttpStatus httpStatus) {
        super(message, cause);
        this.error = error;
        this.id = id;
        this.httpStatus = httpStatus;
    }

    public DummyError(Throwable cause, String error, String id, HttpStatus httpStatus) {
        super(cause);
        this.error = error;
        this.id = id;
        this.httpStatus = httpStatus;
    }

    public DummyError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String error, String id, HttpStatus httpStatus) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.error = error;
        this.id = id;
        this.httpStatus = httpStatus;
    }
}