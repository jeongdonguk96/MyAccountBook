package com.accountbook.myaccountbook.controller;

import com.accountbook.myaccountbook.dto.ResponseDto;
import com.accountbook.myaccountbook.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeApiController {
    private final MemberService memberService;

    // 아이디 중복확인
    @PostMapping("/checkId")
    public ResponseDto<Integer> checkId(@RequestBody String loginId) {
        int result = memberService.checkId(loginId);

        if (result == 1) {
            return new ResponseDto<>(HttpStatus.OK.value(), 1);
        } else {
            return new ResponseDto<>(HttpStatus.OK.value(), 0);
        }
    }
}
