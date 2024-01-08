package com.accountbook.myaccountbook.jwt;

import com.accountbook.myaccountbook.userdetails.CustomUserDetails;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
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

    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtProcess) {
        super(authenticationManager);
        this.jwtService = jwtProcess;
    }


    // 권한 확인을 위해 Authentication 객체를 시큐리티 컨텍스트에 저장한다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        // 쿠키에 토큰이 있을 때만 동작한다.
        if (isCookieIncluded(request)) {

            // 쿠키에서 토큰을 꺼내 파싱한다.
            String replacedAccessToken = getAccessToken(request);

            // 액세스 토큰을 검증하고 CustomUserDetails를 반환한다.
            // 액세스 토큰이 만료되면 리프레시 토큰으로 재발급한다.
            CustomUserDetails userDetails = null;
            try {
                userDetails = jwtService.verifyAccessToken(replacedAccessToken);
            } catch (TokenExpiredException | JWTDecodeException e) {
                e.printStackTrace();
                String refreshToken = getRefreshToken(request);
                userDetails = jwtService.verifyRefreshToken(response, refreshToken);
            }

            // 토큰에서 반환한 CustomUserDetails로 Authentication 객체를 생성하고 시큐리티 컨텍스트에 저장한다.
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }


    // 쿠키에 토큰 존재 여부를 확인한다.
    private boolean isCookieIncluded(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("accessToken".equals(cookie.getName()) || "refreshToken".equals(cookie.getName())) {
                return true;
            }
        }

        return false;
    }


    // 쿠키에서 액세스 토큰을 꺼내 파싱한다.
    private String getAccessToken(HttpServletRequest request) {
        String accessToken = "";
        Cookie[] header = request.getCookies();

        for (Cookie cookie : header) {
            if ("accessToken".equals(cookie.getName())) {
                accessToken = cookie.getValue();
                break;
            }
        }

        return accessToken;
    }


    // 쿠키에서 리프레시 토큰을 꺼내 파싱한다.
    private String getRefreshToken(HttpServletRequest request) {
        String refreshToken = "";
        Cookie[] header = request.getCookies();

        for (Cookie cookie : header) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        return refreshToken;
    }
}
