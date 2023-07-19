package com.accountbook.myaccountbook.jwt;

import com.accountbook.myaccountbook.persistence.Member;
import com.accountbook.myaccountbook.persistence.RoleEnum;
import com.accountbook.myaccountbook.redis.RefreshTokenRepository;
import com.accountbook.myaccountbook.userdetails.CustomUserDetails;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtProcess {

    private static RefreshTokenRepository refreshTokenRepository;


    // 액세스 토큰을 생성한다.
    public static String createAccessToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + JwtVo.ACCESS_TOKEN_EXPIRATION_TIME);

        String accessToken = JWT.create()
                .withSubject("accountBook")
                .withIssuedAt(now)
                .withClaim("mid", userDetails.getMember().getMid())
                .withClaim("role", userDetails.getMember().getRole().toString())
                .withExpiresAt(expiration)
                .sign(Algorithm.HMAC256(JwtVo.SECRET));

        return JwtVo.TOKEN_PREFIX + accessToken;
    }


    // 액세스 토큰을 검증한다.
    public static CustomUserDetails verifyAccessToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(JwtVo.SECRET)).build().verify(token);
        Integer mid = decodedJWT.getClaim("mid").asInt();
        String role = decodedJWT.getClaim("role").asString();
        Member member = Member.builder().mid(mid).role(RoleEnum.valueOf(role)).build();

        return new CustomUserDetails(member);
    }


    // 토큰 만료 시 토큰을 삭제한다.
    public static void checkTokenExpiration(HttpServletResponse response, String replacedToken) {
        DecodedJWT decodedJWT = JWT.decode(replacedToken);
        if (decodedJWT.getExpiresAt().before(new Date())) {
            Cookie cookie = new Cookie("accessToken", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);

            response.addCookie(cookie);
        }
    }


    // 레디스에 리프레시 토큰을 저장하고 생성한다.
    public static String createRefreshToken(CustomUserDetails userDetails) {
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), userDetails.getMember().getMid());

        return refreshTokenRepository.save(refreshToken);
    }

}