package com.accountbook.myaccountbook.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    // 회원가입 화면
    @GetMapping("/join")
    public String getJoinView() {
        return "member/join";
    }


}
