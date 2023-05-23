package com.accountbook.myaccountbook.controller;

import com.accountbook.myaccountbook.service.AccountBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final AccountBookService accountBookService;

    // 초기 화면
    @GetMapping("/")
    public String firstView() {
        return "index";
    }


    // 회원가입 화면
    @GetMapping("/join")
    public String getJoinView() {
        return "member/join";
    }


    // 로그인 화면
    @GetMapping("/login")
    public String getLoginView() {
        return "member/login";
    }


    // 지출 통계 화면
    @GetMapping("/statistics")
    public String getStatisticsView(Model model) {
        return "accountbook/statistics";
    }
}
