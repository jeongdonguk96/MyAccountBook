package com.accountbook.myaccountbook.jwt;

import com.accountbook.myaccountbook.userdetails.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    // 헤더의 Authorization으로 토큰을 검증하고
    // 권한 확인을 위해 Authentication 객체를 시큐리티 컨텍스트에 저장
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        if (isTokenIncluded(request, response)) {
            // 토큰으로 사용자를 검증하고 CustomUserDetails를 반환
            String replacedToken = getJwtToken(request);
            CustomUserDetails userDetails = JwtProcess.verify(replacedToken);

            // 토큰에서 반환한 CustomUserDetails로 Authentication 객체를 생성하고 시큐리티 컨텍스트에 저장
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }


    // 토큰 존재 여부 확인
    private boolean isTokenIncluded(HttpServletRequest request, HttpServletResponse response) {
        String jwtToken = null;
        Cookie[] header = request.getCookies();

        for (Cookie cookie : header) {
            if ("jwt".equals(cookie.getName())) {
                jwtToken = cookie.getValue();
                break;
            }
        }

        return jwtToken != null && jwtToken.startsWith(JwtVo.TOKEN_PREFIX);
    }


    // 토큰 파싱 (쿠키에서 꺼내기)
    private String getJwtToken(HttpServletRequest request) {
        String jwtToken = "";
        Cookie[] header = request.getCookies();

        for (Cookie cookie : header) {
            if ("jwt".equals(cookie.getName())) {
                jwtToken = cookie.getValue();
                break;
            }
        }

        return jwtToken.replace(JwtVo.TOKEN_PREFIX, "");
    }
}
