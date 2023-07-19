package com.accountbook.myaccountbook.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Id;

@Getter
@AllArgsConstructor
public class RefreshToken {
//    private static final long serialVersionUID = -7353484588260422449L;
    @Id
    private String refreshToken;
    private Integer mid;
}
