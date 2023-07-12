package com.accountbook.myaccountbook.apicontroller;

import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.dto.ResponseDto;
import com.accountbook.myaccountbook.dto.member.RequestJoinDto;
import com.accountbook.myaccountbook.dto.member.RequestLoginDto;
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
            return new ResponseDto<>(HttpStatus.OK.value(), 1, "success");
        } else {
            return new ResponseDto<>(HttpStatus.OK.value(), 0, "fail");
        }
    }


    // 회원가입
    @PostMapping("/join")
    public ResponseDto<Integer> join(@RequestBody @Validated RequestJoinDto joinDto) {
        memberService.join(joinDto);

        return new ResponseDto<>(HttpStatus.OK.value(), 1, "success");
    }


//    // 회원가입
//    @PostMapping("/join")
//    public ResponseDto<Integer> join(@RequestBody @Validated RequestJoinDto joinDto) {
//        Job job = new Job(joinDto.getField(), joinDto.getYear(), joinDto.getSalary());
//
//        Member member = Member.builder()
//                            .username(joinDto.getUsername())
//                            .pwd(joinDto.getPwd())
//                            .age(joinDto.getAge())
//                            .job(job)
//                            .build();
//
//        memberService.join(member);
//
//        return new ResponseDto<>(HttpStatus.OK.value(), 1, "success");
//    }


    // 로그인
    @PostMapping("/login")
    public ResponseDto<Integer> login(@RequestBody @Validated RequestLoginDto loginDto, Model model, HttpSession session) {
        Member findMember = memberService.login(loginDto);

        if (findMember != null) {
            model.addAttribute("user", findMember);
            return new ResponseDto<>(HttpStatus.OK.value(), 1, "success");
        } else {
            return new ResponseDto<>(HttpStatus.OK.value(), 0, "success");
        }
    }
}
