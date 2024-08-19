package ru.vtb.jpro.limits;


import lombok.Getter;
import org.springframework.http.HttpStatus;


public class LimitException extends RuntimeException {

    @Getter
    private final HttpStatus status;

    public LimitException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
