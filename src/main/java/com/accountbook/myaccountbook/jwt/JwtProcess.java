package com.accountbook.myaccountbook.jwt;

import com.accountbook.myaccountbook.enums.RoleEnum;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtProcess {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    // 액세스 토큰을 생성한다.
    public String generateAccessToken(Optional<Member> userDetails) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + JwtVo.ACCESS_TOKEN_EXPIRATION_TIME);

        return JWT.create()
                .withSubject("accountBook")
                .withIssuedAt(now)
                .withClaim("mid", userDetails.get().getMid())
                .withClaim("role", userDetails.get().getRole().toString())
                .withExpiresAt(expiration)
                .sign(Algorithm.HMAC256(JwtVo.SECRET));
    }


    // 리프레시 토큰을 생성하고 Redis에 저장한다.
    public String generateRefreshToken(Optional<Member> userDetails) {
        System.out.println("리프레시 토큰 발급됨");
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), userDetails.get().getMid());
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getRefreshToken();
    }


    // 액세스 토큰을 검증해 CustomUserDetails 객체를 반환한다.
    public CustomUserDetails verifyAccessToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(JwtVo.SECRET)).build().verify(token);
        Integer mid = decodedJWT.getClaim("mid").asInt();
        String role = decodedJWT.getClaim("role").asString();
        Member member = Member.builder().mid(mid).role(RoleEnum.valueOf(role)).build();

        return new CustomUserDetails(member);
    }


    // 토큰 만료 시 토큰을 삭제한다.
    public void checkTokenExpiration(HttpServletResponse response, String replacedToken) {
        DecodedJWT decodedJWT = JWT.decode(replacedToken);

        if (decodedJWT.getExpiresAt().before(new Date())) {
            Cookie cookie = new Cookie("accessToken", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);

            response.addCookie(cookie);
        }
    }


    // 쿠키의 리프레시 토큰과 Redis의 리프레시 토큰을 비교 후 재발급한다.
    public CustomUserDetails checkRefreshToken(HttpServletResponse response, String refreshToken) throws IOException {
        // Redis에서 리프레시 토큰을 조회한다.
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findRefreshTokenById(refreshToken);

        // 리프레시 토큰이 있을 시
        if (findRefreshToken.isPresent()) {
            // value인 mid로 Member 객체를 생성한다.
            RefreshToken unWrapedRefreshToken = findRefreshToken.get();
            Object mid = unWrapedRefreshToken.getMid();
            int realMid = (int) mid;
            Optional<Member> findMember = memberRepository.findById((realMid));

            // 기존 리프레시를 삭제한다.
            refreshTokenRepository.deleteById(refreshToken);

            // 새로운 토큰들을 발급한다.
            String newAccessToken = generateAccessToken(findMember);
            String newRefreshToken = generateRefreshToken(findMember);

            // 토큰들을 쿠키에 넣어준다.
//            CookieUtil.addCookie(response, "accessToken", newAccessToken, JwtVo.ACCESS_TOKEN_EXPIRATION_TIME, true, true);
//            CookieUtil.addCookie(response, "refreshToken", newRefreshToken, JwtVo.REFRESH_TOKEN_EXPIRATION_TIME, true, true);

            CookieUtil.addCookie(response, "accessToken", newAccessToken, JwtVo.ACCESS_TOKEN_EXPIRATION_TIME, true, true);
            CookieUtil.addCookie(response, "refreshToken", newRefreshToken, JwtVo.REFRESH_TOKEN_EXPIRATION_TIME, true, true);

            return new CustomUserDetails(findMember.get());
        }
        else {
            response.sendRedirect("/login");
            return null;
        }
    }


    // Redis에서 accessToken이 블랙리스트인지 확인한다.
    // 블랙리스트면 true, 아니면 false를 리턴한다.
    public boolean checkAccessToken(String accessToken) {
        Integer number = refreshTokenRepository.findAccessTokenById(accessToken);
        boolean result = false;

        if (number != null) {
            if (number == 0) {
                System.out.println("this guy is on blacklist");
                result = true;
            }
        }

        return result;
    }

}