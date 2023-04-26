package com.accountbook.myaccountbook.controller;

import com.accountbook.myaccountbook.domain.Job;
import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.dto.ResponseDto;
import com.accountbook.myaccountbook.dto.member.JoinDto;
import com.accountbook.myaccountbook.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseDto<Integer> join(@RequestBody JoinDto joinDto) {
        System.out.println("joinDto = " + joinDto);
        Job job = new Job(joinDto.getField(), joinDto.getYear(), joinDto.getSalary());
        Member member = new Member(joinDto.getLoginId(), joinDto.getPwd(), joinDto.getName(), joinDto.getAge(), job);

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
