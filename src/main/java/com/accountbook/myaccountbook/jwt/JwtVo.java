package com.accountbook.myaccountbook.jwt;

public interface JwtVo {
    public static final String SECRET = "accoutBook";
    public static final int EXPIRATION_TIME = 1000 * 60 * 5; // 5분
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER = "Authorization";
}
