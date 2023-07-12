package com.accountbook.myaccountbook.jwt;

import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.domain.RoleEnum;
import com.accountbook.myaccountbook.userdetails.CustomUserDetails;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtProcess {

    // 토큰 생성
    public static String create(CustomUserDetails userDetails) {
        String jwtToken = JWT.create()
                .withSubject("accountBook")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVo.EXPIRATION_TIME))
                .withClaim("mid", userDetails.getMember().getMid())
                .withClaim("role", userDetails.getMember().getRole().toString())
                .sign(Algorithm.HMAC256(JwtVo.SECRET));

        return JwtVo.TOKEN_PREFIX + jwtToken;
    }


    // 토큰 검증
    public static CustomUserDetails verify(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(JwtVo.SECRET)).build().verify(token);
        Integer mid = decodedJWT.getClaim("mid").asInt();
        String role = decodedJWT.getClaim("role").asString();
        Member member = Member.builder().mid(mid).role(RoleEnum.valueOf(role)).build();

        return new CustomUserDetails(member);
    }
}
