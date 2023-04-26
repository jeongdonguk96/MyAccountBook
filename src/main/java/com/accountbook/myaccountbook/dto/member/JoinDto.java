package com.accountbook.myaccountbook.dto.member;

import lombok.Data;

@Data
public class JoinDto {
    private String loginId;
    private String pwd;
    private String name;
    private int age;
    private String field;
    private int year;
    private int salary;
}
