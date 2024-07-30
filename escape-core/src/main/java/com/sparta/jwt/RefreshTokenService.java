package com.sparta.jwt;

import com.sparta.domain.user.entity.User;
import com.sparta.domain.user.repository.UserRepository;
import com.sparta.global.exception.customException.RefreshTokenException;
import com.sparta.global.exception.errorCode.RefreshTokenErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {
    private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";
    // 만료 시간 7일
    private static final long refreshTokenTTL = 7 * 24 * 60 * 60 * 1000L;
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveRefreshTokenInfo(String email, String newRefreshToken) {
        String key = makeRefreshTokenKey(email);
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(newRefreshToken)
                .expiration(refreshTokenTTL)
                .build();

        redisTemplate.opsForValue().set(key, refreshToken, refreshTokenTTL, TimeUnit.MILLISECONDS);
    }

    public boolean isRefreshTokenPresent(String email) {
        RefreshToken tokenInfo = (RefreshToken) redisTemplate.opsForValue().get(makeRefreshTokenKey(email));

        if(tokenInfo == null) {
            return false;
        }

        return true;
    }

    public void deleteRefreshTokenInfo(String email) {
        redisTemplate.delete(makeRefreshTokenKey(email));
    }

    private String makeRefreshTokenKey(String email) {
        return REFRESH_TOKEN_PREFIX + email;
    }

    //    public void updateRefreshToken(RefreshToken refreshToken) {
//        redisTemplate.opsForValue().set(refreshToken.getEmail(), refreshToken, refreshToken.getExpiration(), TimeUnit.MILLISECONDS);
//    }
//    @Transactional
//    public void saveRefreshToken(User user, String refreshToken) {
//        Optional<RefreshToken> existToken = findByEmail(user.getEmail());
//
//        if (existToken.isPresent()) {
//            existToken.get().update(refreshToken);
//        } else {
//            refreshTokenRepository.save(new RefreshToken(user.getEmail(), refreshToken));
//        }
//    }
//
//    @Transactional
//    public void deleteToken(String email) {
//        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email).orElseThrow( //
//                () -> new RefreshTokenException(RefreshTokenErrorCode.REFRESH_TOKEN_NOT_FOUND)
//        );
//        refreshTokenRepository.delete(refreshToken);
//    }
//
//    public String createRefreshToken() {
//        return UUID.randomUUID().toString();
//    }
}
