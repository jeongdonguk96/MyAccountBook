package com.accountbook.myaccountbook.jwt;

import com.accountbook.myaccountbook.dto.member.RequestLoginDto;
import com.accountbook.myaccountbook.userdetails.CustomUserDetails;
import com.accountbook.myaccountbook.util.CustomResponseUtil;
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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login");
        this.authenticationManager = authenticationManager;
    }


    // 로그인 시 동작된다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // 로그인 요청 시 들어온 데이터를 Dto로 변환한다.
            RequestLoginDto loginDto = objectMapper.readValue(request.getInputStream(), RequestLoginDto.class);

            // 강제 로그인을 시킨다.
            // UsernamePasswordAuthenticationToken는 Authentication 객체를 상속한 객체다.
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(), loginDto.getPwd());

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
        String accessToken = JwtProcess.generateAccessToken(userDetails);
        String refreshToken = JwtProcess.generateRefreshToken(userDetails);

        // 생성한 액세스/리프레시 토큰을 브라우저 쿠키에 저장한다.
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(JwtVo.ACCESS_TOKEN_EXPIRATION_TIME);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(JwtVo.REFRESH_TOKEN_EXPIRATION_TIME);
        response.addCookie(refreshTokenCookie);


        // 프론트에 응답한다.
        CustomResponseUtil.success(response, accessToken, "로그인 성공");
    }


    // attemptAuthentication에서 try-catch에 걸려서 예외를 던질 때 호출됨 (로그인 실패 시)
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        CustomResponseUtil.fail(response, HttpStatus.UNAUTHORIZED, "로그인 실패");
    }
}
