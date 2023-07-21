package com.accountbook.myaccountbook.apicontroller;

import com.accountbook.myaccountbook.dto.ResponseDto;
import com.accountbook.myaccountbook.dto.member.RequestJoinDto;
import com.accountbook.myaccountbook.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api")
@SessionAttributes("user")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;


    // 아이디 중복확인을 한다.
    @PostMapping("/checkId")
    public ResponseDto<Integer> checkId(String joinId) {
        int result = memberService.checkId(joinId);

        if (result == 1) {
            return new ResponseDto<>(HttpStatus.OK.value(), null, "success");
        } else {
            return new ResponseDto<>(HttpStatus.CONFLICT.value(), null, "fail");
        }
    }


    // 회원가입을 한다.
    @PostMapping("/join")
    public ResponseDto<Integer> join(@RequestBody @Validated RequestJoinDto joinDto) {
        memberService.join(joinDto);

        return new ResponseDto<>(HttpStatus.CREATED.value(), null, "success");
    }


    // 리프레시 토큰을 검증한다.
    @PostMapping("/refreshToken")
    public ResponseDto<Integer> validateRefreshToken(HttpServletRequest request,
                                                     HttpServletResponse response) throws IOException {

        // 쿠키에서 리프레시 토큰을 꺼낸다.
        String refreshToken_header = null;
        Cookie[] header = request.getCookies();

        for (Cookie cookie : header) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken_header = cookie.getValue();
                break;
            }
        }

        // 쿠키에서 꺼낸 리프레시 토큰과 Redis에 저장된 리프레시 토큰을 비교한다.
//        RedisT

        // 검증에 성공하면 JwtAuthenticationFilter를 타게 하고,
        response.sendRedirect("api/login");

        // 검증에 실패하면 로그인 화면으로 보낸다.
        response.sendRedirect("login");

        return new ResponseDto<>(HttpStatus.CREATED.value(), null, "success");
    }

}
