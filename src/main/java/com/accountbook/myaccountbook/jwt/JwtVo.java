package com.accountbook.myaccountbook.jwt;

public interface JwtVo {
    public static final String SECRET = "accoutBook";
    public static final int ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 5;
    public static final int REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60;
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER = "Authorization";
}
