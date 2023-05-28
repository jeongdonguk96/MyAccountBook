package com.accountbook.myaccountbook.dto.member;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class JoinDto {

    @NotBlank
    @Size(max = 20)
    private String loginId;

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
    @Range(max = 2)
    private int year;

    @NotNull
    private int salary;
}
