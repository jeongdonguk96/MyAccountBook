package com.accountbook.myaccountbook.controller;

import com.accountbook.myaccountbook.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;


    // 인덱스 화면을 보여준다.
    @GetMapping("/")
    public String firstView() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "index";
    }


    // 회원가입 화면을 보여준다.
    @GetMapping("/join")
    public String getJoinView() {
        return "member/join";
    }


    // 로그인 화면을 보여준다.
    @GetMapping("/login")
    public String getLoginView() {
        return "member/login";
    }


    // 로그아웃 처리한다.
    @GetMapping("/api/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accessToken = null;
        String refreshToken = null;
        Cookie[] header = request.getCookies();

        // 헤더에서 액세스 토큰과 리프레시 토큰을 꺼낸다.
        for (Cookie cookie : header) {
            if ("accessToken".equals(cookie.getName())) {
                accessToken = cookie.getValue();
            }
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
            }
            if (accessToken != null & refreshToken != null) {
                break;
            }
        }

        // 레디스에서 리프레시 토큰을 삭제하고, 쿠키에서 액세스 토큰과 리프레시 토큰을 만료시킨다.
        memberService.logout(response, accessToken, refreshToken);

        response.sendRedirect("/");
    }


    // 권한 예외를 처리한다.
    @GetMapping("/noAuthorization")
    public String noAuthorization() {
        return "error/denied";
    }

    // 권한 테스트용
    @GetMapping("/book2")
    public String book2() {
        return "accountbook/book2";
    }
}
