package com.accountbook.myaccountbook.jwt;

import com.accountbook.myaccountbook.persistence.Member;
import com.accountbook.myaccountbook.persistence.RoleEnum;
import com.accountbook.myaccountbook.userdetails.CustomUserDetails;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class JwtProcess {



    // 액세스 토큰 생성
    public static String createAccessToken(CustomUserDetails userDetails) {
        String accessToken = JWT.create()
                .withSubject("accountBook")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVo.ACCESS_TOKEN_EXPIRATION_TIME))
                .withClaim("mid", userDetails.getMember().getMid())
                .withClaim("role", userDetails.getMember().getRole().toString())
                .withClaim("expiration", new Date(System.currentTimeMillis() + JwtVo.ACCESS_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(JwtVo.SECRET));

        return JwtVo.TOKEN_PREFIX + accessToken;
    }


    // 액세스 토큰 검증
    public static CustomUserDetails verifyAccessToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(JwtVo.SECRET)).build().verify(token);
        Integer mid = decodedJWT.getClaim("mid").asInt();
        String role = decodedJWT.getClaim("role").asString();
        Member member = Member.builder().mid(mid).role(RoleEnum.valueOf(role)).build();

        return new CustomUserDetails(member);
    }


    // 토큰 만료 시 토큰 삭제
    public static void checkTokenExpirationAndDelete(HttpServletResponse response, String replacedToken) {
        DecodedJWT decodedJWT = JWT.decode(replacedToken);
        System.out.println(new Date());
        System.out.println(decodedJWT.getExpiresAt());
        if (decodedJWT.getExpiresAt().before(new Date())) {
            Cookie cookie = new Cookie("accessToken", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);

            response.addCookie(cookie);
        }
    }


    // 리프레시 토큰 생성
    public static String createRefreshToken(CustomUserDetails userDetails) {
        return JWT.create()
                .withSubject("accountBook")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVo.REFRESH_TOKEN_EXPIRATION_TIME))
                .withClaim("mid", userDetails.getMember().getMid())
                .withClaim("expiration", new Date(System.currentTimeMillis() + JwtVo.REFRESH_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(JwtVo.SECRET));
    }

}