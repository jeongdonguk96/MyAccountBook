package com.accountbook.myaccountbook.apicontroller;

import com.accountbook.myaccountbook.domain.Job;
import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.dto.ResponseDto;
import com.accountbook.myaccountbook.dto.member.JoinDto;
import com.accountbook.myaccountbook.dto.member.LoginDto;
import com.accountbook.myaccountbook.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/api/member")
@SessionAttributes("user")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;


    // 아이디 중복확인
    @PostMapping("/checkId")
    public ResponseDto<Integer> checkId(String joinId) {
        int result = memberService.checkId(joinId);

        if (result == 1) {
            return new ResponseDto<>(HttpStatus.OK.value(), 1);
        } else {
            return new ResponseDto<>(HttpStatus.OK.value(), 0);
        }
    }


    // 회원가입
    @PostMapping("/join")
    public ResponseDto<Integer> join(@RequestBody @Validated JoinDto joinDto) {
        Job job = new Job(joinDto.getField(), joinDto.getYear(), joinDto.getSalary());

        Member member = Member.builder()
                            .loginId(joinDto.getLoginId())
                            .pwd(joinDto.getPwd())
                            .age(joinDto.getAge())
                            .job(job)
                            .build();

        memberService.join(member);

        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }


    // 로그인
    @PostMapping("/login")
    public ResponseDto<Integer> login(@RequestBody @Validated LoginDto loginDto, Model model, HttpSession session) {
        Member findMember = memberService.login(loginDto);

        if (findMember != null) {
            model.addAttribute("user", findMember);
            return new ResponseDto<>(HttpStatus.OK.value(), 1);
        } else {
            return new ResponseDto<>(HttpStatus.OK.value(), 0);
        }
    }
}
