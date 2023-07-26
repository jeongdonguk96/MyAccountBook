package com.accountbook.myaccountbook.redis;

import com.accountbook.myaccountbook.jwt.JwtVo;
import com.accountbook.myaccountbook.jwt.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final RedisTemplate<String, Integer> redisTemplate;


    // 레디스에 리프레시 토큰을 TTL과 함께 저장한다.
    public String save(RefreshToken refreshToken) {
        ValueOperations<String, Integer> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken.getRefreshToken(), refreshToken.getMid());
        redisTemplate.expire(refreshToken.getRefreshToken(), JwtVo.REFRESH_TOKEN_EXPIRATION_TIME, TimeUnit.SECONDS);

        return refreshToken.getRefreshToken();
    }


    // 레디스에서 리프레시 토큰을 찾는다.
    public Optional<RefreshToken> findRefreshTokenById(String refreshToken) {
        ValueOperations<String, Integer> valueOperations = redisTemplate.opsForValue();
        Integer mid = valueOperations.get(refreshToken);

        if (Objects.isNull(mid)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(refreshToken, mid));
    }


    // 레디스에서 리프레시 토큰을 지운다.
    public void deleteById(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }
}
