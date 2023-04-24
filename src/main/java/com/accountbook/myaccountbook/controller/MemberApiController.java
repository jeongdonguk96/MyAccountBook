package com.accountbook.myaccountbook.controller;

import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.dto.ResponseDto;
import com.accountbook.myaccountbook.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping("/join")
    public ResponseDto<Integer> join(Member member) {
        memberService.join(member);

        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseDto<Integer> login(Member member, HttpSession session) {
        int result = memberService.login(member);

        if (result == 1) {
            session.setAttribute("user", member);
            return new ResponseDto<>(HttpStatus.OK.value(), 1);
        }
        else {
            return new ResponseDto<>(HttpStatus.OK.value(), -1);
        }
    }

}
