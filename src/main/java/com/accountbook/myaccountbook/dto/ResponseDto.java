package com.accountbook.myaccountbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDto<T> {
    private final int code;
    private final T data;
    private final String message;
}
