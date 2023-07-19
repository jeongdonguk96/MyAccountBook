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
import java.util.Arrays;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    // 권한 확인을 위해 Authentication 객체를 시큐리티 컨텍스트에 저장한다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String[] whiteList = {
                "/", "/join", "/login",
                "/api/join", "/api/checkId", "/error"
        };

        // 화이트리스트를 확인한다.
        boolean whiteListUri = checkUri(request, whiteList);

        // 화이트리스트가 아니고 액세스 토큰이 있을 때만 동작한다.
        if (!whiteListUri && isTokenIncluded(request)) {
            // 쿠키에서 토큰을 꺼내 파싱한다.
            String replacedAccessToken = getAccessToken(request);

            // 토큰을 검증하고 CustomUserDetails를 반환한다.
            CustomUserDetails userDetails = JwtProcess.verifyAccessToken(replacedAccessToken);

            // 토큰에서 반환한 CustomUserDetails로 Authentication 객체를 생성하고 시큐리티 컨텍스트에 저장한다.
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }


    // 토큰 존재 여부를 확인한다.
    private boolean isTokenIncluded(HttpServletRequest request) {
        String accessToken = null;
        Cookie[] header = request.getCookies();

        for (Cookie cookie : header) {
            if ("accessToken".equals(cookie.getName())) {
                accessToken = cookie.getValue();
                break;
            }
        }

        return accessToken != null && accessToken.startsWith(JwtVo.TOKEN_PREFIX);
    }


    // 쿠키에서 토큰을 꺼내 파싱한다.
    private String getAccessToken(HttpServletRequest request) {
        String accessToken = "";
        Cookie[] header = request.getCookies();

        for (Cookie cookie : header) {
            if ("accessToken".equals(cookie.getName())) {
                accessToken = cookie.getValue();
                break;
            }
        }

        return accessToken.replace(JwtVo.TOKEN_PREFIX, "");
    }


    // 요청 Uri가 화이트리스트인지 확인한다.
    private static boolean checkUri(HttpServletRequest request, String[] whiteList) {
        return Arrays.stream(whiteList)
                .anyMatch(uri -> request.getRequestURI().equals(uri));
    }
}
