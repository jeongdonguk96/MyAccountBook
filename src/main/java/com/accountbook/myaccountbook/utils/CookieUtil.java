package com.accountbook.myaccountbook.utils;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class CookieUtil {

    // 사용자의 브라우저에 쿠키를 담는다.
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge, boolean httpOnly, boolean secure) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        response.addCookie(cookie);
    }


    // 사용자의 브라우저에서 쿠키를 제거한다.
    public static void removeCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
