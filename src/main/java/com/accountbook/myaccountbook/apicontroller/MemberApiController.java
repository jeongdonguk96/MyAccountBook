package com.accountbook.myaccountbook.apicontroller;

import com.accountbook.myaccountbook.dto.ResponseDto;
import com.accountbook.myaccountbook.dto.member.RequestJoinDto;
import com.accountbook.myaccountbook.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@SessionAttributes("user")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;


    // 아이디 중복확인
    @PostMapping("/checkId")
    public ResponseDto<Integer> checkId(String joinId) {
        int result = memberService.checkId(joinId);

        if (result == 1) {
            return new ResponseDto<>(HttpStatus.OK.value(), null, "success");
        } else {
            return new ResponseDto<>(HttpStatus.CONFLICT.value(), null, "fail");
        }
    }


    // 회원가입
    @PostMapping("/join")
    public ResponseDto<Integer> join(@RequestBody @Validated RequestJoinDto joinDto) {
        memberService.join(joinDto);

        return new ResponseDto<>(HttpStatus.CREATED.value(), null, "success");
    }

}
