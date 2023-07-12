package com.accountbook.myaccountbook.handler;

import com.accountbook.myaccountbook.dto.ResponseDto;
import com.accountbook.myaccountbook.exception.CustomApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomErrorHandler {

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(CustomApiException e) {

        return new ResponseEntity<>(new ResponseDto<>(-1, HttpStatus.BAD_REQUEST, e.getMessage()).getData());
    }
}
