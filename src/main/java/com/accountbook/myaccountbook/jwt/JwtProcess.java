package com.accountbook.myaccountbook.jwt;

import com.accountbook.myaccountbook.exception.CustomTokenExpiredException;
import com.accountbook.myaccountbook.persistence.Member;
import com.accountbook.myaccountbook.persistence.RoleEnum;
import com.accountbook.myaccountbook.redis.RefreshTokenRepository;
import com.accountbook.myaccountbook.userdetails.CustomUserDetails;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtProcess {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 액세스 토큰을 생성한다.
    public static String generateAccessToken(CustomUserDetails userDetails) {
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


    // 액세스 토큰을 검증해 CustomUserDetails 객체를 반환한다.
    public static CustomUserDetails verifyAccessToken(String token) {

        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(JwtVo.SECRET)).build().verify(token);
            Integer mid = decodedJWT.getClaim("mid").asInt();
            String role = decodedJWT.getClaim("role").asString();
            Member member = Member.builder().mid(mid).role(RoleEnum.valueOf(role)).build();

            return new CustomUserDetails(member);
        } catch (TokenExpiredException e) {
            throw new CustomTokenExpiredException("TokenExpiredException");
        }
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
    public static String generateRefreshToken(CustomUserDetails userDetails) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        RefreshTokenRepository refreshTokenRepository = new RefreshTokenRepository(redisTemplate);

        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), userDetails.getMember().getMid());

        refreshTokenRepository.save(refreshToken);

        return refreshToken.getRefreshToken();
    }

}