package com.accountbook.myaccountbook.jwt;

import com.accountbook.myaccountbook.userdetails.CustomUserDetails;
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
import java.util.Arrays;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtProcess jwtProcess;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProcess jwtProcess) {
        super(authenticationManager);
        this.jwtProcess = jwtProcess;
    }


    // 권한 확인을 위해 Authentication 객체를 시큐리티 컨텍스트에 저장한다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

//        String[] whiteList = {
//                "/", "/join", "/login", "/error",
//                "/api/join", "/api/checkId", "api/refreshToken"
//        };
//
//        // 화이트리스트를 확인한다.
//        boolean whiteListUri = checkUri(request, whiteList);

        // 화이트리스트가 아니고 액세스 토큰이 있을 때만 동작한다.
//        if (!whiteListUri && isTokenIncluded(request)) {

        // 액세스 토큰이 있을 때만 동작한다.
        if (isTokenIncluded(request)) {

            // 쿠키에서 토큰을 꺼내 파싱한다.
            String replacedAccessToken = getAccessToken(request);

            // 액세스 토큰을 검증하고 CustomUserDetails를 반환한다.
            // 액세스 토큰이 만료되면 리프레시 토큰으로 재발급한다.
            CustomUserDetails userDetails = null;
            try {
                userDetails = jwtProcess.verifyAccessToken(replacedAccessToken);
            } catch (TokenExpiredException e) {
                String refreshToken = getRefreshToken(request);
                userDetails = jwtProcess.checkRefreshToken(response, refreshToken);
            }

            // 토큰에서 반환한 CustomUserDetails로 Authentication 객체를 생성하고 시큐리티 컨텍스트에 저장한다.
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }


    // 액세스 토큰 존재 여부를 확인한다.
    private boolean isTokenIncluded(HttpServletRequest request) {
        String accessToken = null;
        Cookie[] header = request.getCookies();

        if (header != null) {
            for (Cookie cookie : header) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }

        return accessToken != null;
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


    // 액세스 토큰이 블랙리스트가 아닌지 판별한다.
    // 블랙리스트면 true, 아니면 false를 리턴한다.
    private boolean isBlacklistToken(HttpServletRequest request) {
        String accessToken = "";
        Cookie[] header = request.getCookies();

        for (Cookie cookie : header) {
            if ("accessToken".equals(cookie.getName())) {
                accessToken = cookie.getValue();
                break;
            }
        }

        return jwtProcess.checkAccessToken(accessToken);
    }

    // 요청 Uri가 화이트리스트인지 확인한다.
    private static boolean checkUri(HttpServletRequest request, String[] whiteList) {
        return Arrays.stream(whiteList)
                .anyMatch(uri -> request.getRequestURI().equals(uri));
    }
}
