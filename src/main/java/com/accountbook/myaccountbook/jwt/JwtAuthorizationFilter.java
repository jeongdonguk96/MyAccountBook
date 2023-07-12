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
import java.util.Base64;

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
//            String replacedToken = request.getHeader(JwtVo.HEADER).replace(JwtVo.TOKEN_PREFIX, "");
            String replacedToken = getJwtToken(request);
//            CustomUserDetails userDetails = JwtProcess.verify(replacedToken);
            System.out.println("### replacedToken = " + replacedToken);
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
                String encodedJwtToken = cookie.getValue();
                System.out.println("encodedJwtToken = " + encodedJwtToken);
                byte[] decodedJwtTokenBytes = Base64.getDecoder().decode(encodedJwtToken);
                jwtToken = new String(decodedJwtTokenBytes);
                break;
            }
        }
        System.out.println("jwtToken = " + jwtToken);

        return jwtToken != null && jwtToken.startsWith(JwtVo.TOKEN_PREFIX);
    }
//    private boolean isTokenIncluded(HttpServletRequest request, HttpServletResponse response) {
//        String header = request.getHeader(JwtVo.HEADER);
//
//        return header != null && header.startsWith(JwtVo.TOKEN_PREFIX);
//    }


    // 토큰 파싱 (쿠키에서 꺼내서 디코딩)
    private String getJwtToken(HttpServletRequest request) {
        String jwtToken = "";
        Cookie[] header = request.getCookies();
        for (Cookie cookie : header) {
            if ("jwt".equals(cookie.getName())) {
                jwtToken = cookie.getValue();
//                System.out.println("encodedJwtToken = " + encodedJwtToken);
//                byte[] decodedJwtTokenBytes = Base64.getDecoder().decode(encodedJwtToken);
//                jwtToken = new String(decodedJwtTokenBytes);
//                break;
            }
        }
        System.out.println("jwtToken = " + jwtToken);

        String replacedToken = jwtToken.replace(JwtVo.TOKEN_PREFIX, "");
        System.out.println("replacedToken = " + replacedToken);

        return jwtToken;
    }
}
