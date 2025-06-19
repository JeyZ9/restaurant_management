package com.app.restaurant_management.commons.exception;

public class AuthenticationFailException extends Exception {
    public AuthenticationFailException(String msg){
        super(msg);
    }
}
