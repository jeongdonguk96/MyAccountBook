package com.accountbook.myaccountbook.jwt;

public interface JwtVo {
    public static final String SECRET = "accountBook";
    public static final int ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 10; // 밀리세컨드
    public static final int ACCESS_TOKEN_MAX_AGE = 60; // 초
    public static final int REFRESH_TOKEN_EXPIRATION_TIME = 60; // 초
    public static final int REFRESH_TOKEN_MAX_AGE = 60; // 초
}
