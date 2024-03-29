package com.accountbook.myaccountbook.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {
    CUSTOMER("고객"), ADMIN("관리자");

    private String value;
}
