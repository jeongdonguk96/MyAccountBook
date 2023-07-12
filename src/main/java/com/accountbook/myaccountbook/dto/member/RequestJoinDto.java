package com.accountbook.myaccountbook.dto.member;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestJoinDto {

    @NotBlank
    @Size(max = 15)
    private String username;

    @NotBlank
    @Size(max = 12)
    private String pwd;

    @NotBlank
    private String name;

    @NotNull
    @Max(100)
    private int age;

    @NotBlank
    private String field;

    @NotNull
    private int year;

    @NotNull
    private int salary;
}
