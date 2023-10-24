package com.accountbook.myaccountbook.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    private String refreshToken;
    private Integer mid;
}
