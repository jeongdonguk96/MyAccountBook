package com.accountbook.myaccountbook.jwt;

import com.accountbook.myaccountbook.dto.member.LoginRequestDto;
import com.accountbook.myaccountbook.entity.Member;
import com.accountbook.myaccountbook.userdetails.CustomUserDetails;
import com.accountbook.myaccountbook.utils.CookieUtil;
import com.accountbook.myaccountbook.utils.CustomResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtProcess) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login");
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtProcess;
    }


    // 로그인 시 동작된다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // 로그인 요청 시 들어온 데이터를 Dto로 변환한다.
            LoginRequestDto loginDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);

            // 강제 로그인을 시킨다.
            // UsernamePasswordAuthenticationToken는 Authentication 객체를 상속한 객체다.
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginDto.username(), loginDto.pwd());

            // 시큐리티 내부 로직으로 authenticate()는 UserDetailsService의 loadUserByUsername()을 호출한다.
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }


    // 위 attemptAuthentication()가 성공해서 Authentication 객체를 반환하면 호출된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        // 액세스/리프레시 토큰을 생성한다.
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        Optional<Member> member = Optional.ofNullable(userDetails.getMember());
        String accessToken = jwtService.generateAccessToken(member);
        String refreshToken = jwtService.generateRefreshToken(member);

        // 생성한 액세스/리프레시 토큰을 브라우저 쿠키에 저장한다.
        CookieUtil.addCookie(response, "accessToken", accessToken, JwtConstant.ACCESS_TOKEN_MAX_AGE, true, true);
        CookieUtil.addCookie(response, "refreshToken", refreshToken, JwtConstant.REFRESH_TOKEN_MAX_AGE, true, true);

        // 프론트에 응답한다.
        CustomResponseUtil.success(response, accessToken, "로그인 성공");
    }


    // attemptAuthentication에서 try-catch에 걸려서 예외를 던질 때 호출된다. (로그인 실패 시)
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        CustomResponseUtil.fail(response, HttpStatus.UNAUTHORIZED, "로그인 실패");
    }
}
