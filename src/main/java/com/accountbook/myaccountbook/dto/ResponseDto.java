package com.accountbook.myaccountbook.dto;

public record ResponseDto<T>(
    int code,
    T data,
    String message
) {
}
