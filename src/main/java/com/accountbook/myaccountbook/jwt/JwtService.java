package com.accountbook.myaccountbook.jwt;

import com.accountbook.myaccountbook.persistence.Member;
import com.accountbook.myaccountbook.redis.RefreshTokenRepository;
import com.accountbook.myaccountbook.repository.MemberRepository;
import com.accountbook.myaccountbook.userdetails.CustomUserDetails;
import com.accountbook.myaccountbook.utils.CookieUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    // 액세스 토큰을 생성한다.
    public String generateAccessToken(Optional<Member> userDetails) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + JwtConstant.ACCESS_TOKEN_EXPIRATION_TIME);

        return JWT.create()
                .withSubject("accountBook")
                .withIssuedAt(now)
                .withClaim("mid", userDetails.get().getMid())
                .withClaim("role", userDetails.get().getRole().toString())
                .withExpiresAt(expiration)
                .sign(Algorithm.HMAC256(JwtConstant.SECRET));
    }


    // 리프레시 토큰을 생성하고 Redis에 저장한다.
    public String generateRefreshToken(Optional<Member> userDetails) {
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), userDetails.get().getMid());
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getRefreshToken();
    }


    // 액세스 토큰을 검증해 CustomUserDetails 객체를 반환한다.
    public CustomUserDetails verifyAccessToken(String token) {
        // 액세스 토큰에서 mid를 꺼낸다.
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(JwtConstant.SECRET)).build().verify(token);
        Integer mid = decodedJWT.getClaim("mid").asInt();

        // 꺼낸 mid로 DB를 조회해 Member 객체를 생성한다.
        Optional<Member> findMember = memberRepository.findById(mid);
        Member member = new Member(findMember);

        // DB에서 찾은 멤버 객체를 반환한다.
        return new CustomUserDetails(member);
    }


    // 쿠키의 리프레시 토큰과 Redis의 리프레시 토큰을 비교 후 재발급한다.
    public CustomUserDetails verifyRefreshToken(HttpServletResponse response, String refreshToken) throws IOException {
        // Redis에서 리프레시 토큰을 조회한다.
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findRefreshTokenById(refreshToken);

        // 리프레시 토큰이 있을 시
        if (findRefreshToken.isPresent()) {
            // value인 mid로 Member 객체를 생성한다.
            RefreshToken unWrapedRefreshToken = findRefreshToken.get();
            Object mid = unWrapedRefreshToken.getMid();
            Optional<Member> findMember = memberRepository.findById(((int) mid));

            // 기존 리프레시 토큰을 삭제한다.
            refreshTokenRepository.deleteById(refreshToken);

            // 액세스 토큰와 리프레시 토큰을 재발급한다.
            String newAccessToken = generateAccessToken(findMember);
            String newRefreshToken = generateRefreshToken(findMember);

            // 토큰들을 쿠키에 넣어준다.
            CookieUtil.addCookie(response, "accessToken", newAccessToken, JwtConstant.ACCESS_TOKEN_MAX_AGE, true, true);
            CookieUtil.addCookie(response, "refreshToken", newRefreshToken, JwtConstant.REFRESH_TOKEN_MAX_AGE, true, true);

            return new CustomUserDetails(findMember.get());
        }
        else {
            return null;
        }
    }
}