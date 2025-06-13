package com.app.restaurant_management.commons.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiErrorException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public ApiErrorException(HttpStatus status, String message){
        this.status = status;
        this.message = message;
    }

    public ApiErrorException(String runtimeMessage, HttpStatus status, String message){
        super(runtimeMessage);
        this.status = status;
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
