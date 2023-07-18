package com.accountbook.myaccountbook.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisDao {

    private final RedisTemplate<String ,String> redisTemplate;
    private final ValueOperations<String, String> operations = redisTemplate.opsForValue();

    
    // 리프레시 토큰 저장
    public void insertRefreshToken(String key, String value) {
        operations.set(key, value);
    }

    // 리프레시 토큰 조회
    public String getRefreshToken(String key) {
        return operations.get(key);
    }

    // 리프레시 토큰 삭제
    public void deleteRefreshToken(String key) {
        redisTemplate.delete(key);
    }
}
