package com.accountbook.myaccountbook.exception;

public class CustomTokenExpiredException extends RuntimeException{

    public CustomTokenExpiredException(String message) {
        super(message);
    }
}
