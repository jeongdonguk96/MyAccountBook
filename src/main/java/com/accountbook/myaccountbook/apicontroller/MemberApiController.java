package com.accountbook.myaccountbook.apicontroller;

import com.accountbook.myaccountbook.domain.Job;
import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.dto.ResponseDto;
import com.accountbook.myaccountbook.dto.member.JoinDto;
import com.accountbook.myaccountbook.dto.member.LoginDto;
import com.accountbook.myaccountbook.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/member")
@SessionAttributes("user")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;


    // 아이디 중복확인
    @PostMapping("/checkId")
    public ResponseDto<Integer> checkId(String loginId) {
        int result = memberService.checkId(loginId);

        if (result == 1) {
            return new ResponseDto<>(HttpStatus.OK.value(), 1);
        } else {
            return new ResponseDto<>(HttpStatus.OK.value(), 0);
        }
    }


    // 회원가입
    @PostMapping("/join")
    public ResponseDto<Integer> join(@RequestBody JoinDto joinDto) {
        Job job = new Job(joinDto.getField(), joinDto.getYear(), joinDto.getSalary());
        Member member = new Member(joinDto.getLoginId(), joinDto.getPwd(), joinDto.getName(), joinDto.getAge(), job);

        memberService.join(member);

        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }


    // 로그인
    @PostMapping("/login")
    public ResponseDto<Integer> login(@RequestBody LoginDto loginDto, Model model, HttpSession session) {
        Member findMember = memberService.login(loginDto);

        if (findMember != null) {
            model.addAttribute("user", findMember);
            return new ResponseDto<>(HttpStatus.OK.value(), 1);
        } else {
            return new ResponseDto<>(HttpStatus.OK.value(), 0);
        }
    }

}
