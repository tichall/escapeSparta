package com.sparta.global.jwt;

import com.sparta.global.exception.customException.AuthException;
import com.sparta.global.exception.errorCode.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.sparta.global.jwt.JwtProvider.BEARER_PREFIX;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

  private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";
  private static final long REFRESH_TOKEN_TTL = 7 * 24 * 60 * 60 * 1000L; // 만료 시간 7일

  private final RedisTemplate<String, Object> redisTemplate;

  /**
   * Redis에 리프레시 토큰 저장
   *
   * @param email        유저의 이메일
   * @param refreshToken 저장할 토큰값
   */
  public void saveRefreshTokenInfo(String email, String refreshToken) {
    String parsedRefreshToken = refreshToken.substring(BEARER_PREFIX.length());
    String key = makeRefreshTokenKey(email);

    RefreshTokenInfo refreshTokenInfo = RefreshTokenInfo.builder()
        .refreshToken(parsedRefreshToken)
        .expiration(REFRESH_TOKEN_TTL)
        .build();

    redisTemplate.opsForValue()
        .set(key, refreshTokenInfo, REFRESH_TOKEN_TTL, TimeUnit.MILLISECONDS);
  }

  /**
   * 리프레시 토큰 존재 여부 확인
   *
   * @param email 유저의 이메일
   * @return 리프레시 토큰 존재 여부
   */
  public boolean isRefreshTokenPresent(String email) {
    RefreshTokenInfo tokenInfo = (RefreshTokenInfo) redisTemplate.opsForValue()
        .get(makeRefreshTokenKey(email));
    return tokenInfo != null;
  }

  /**
   * 전달된 리프레시 토큰과 Redis에 저장된 리프레시 토큰 검증
   *
   * @param email        유저의 이메일
   * @param refreshToken 헤더로 전달된 리프레시 토큰
   */
  public void checkValidRefreshToken(String email, String refreshToken) {
    RefreshTokenInfo tokenInfo = (RefreshTokenInfo) redisTemplate.opsForValue()
        .get(makeRefreshTokenKey(email));

    if (tokenInfo == null) {
      throw new AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND);
    } else {
      if (!refreshToken.equals(tokenInfo.getRefreshToken())) {
        throw new AuthException(AuthErrorCode.REFRESH_TOKEN_MISMATCH);
      }
    }
  }

  /**
   * Redis에서 리프레시 토큰 삭제
   *
   * @param email 유저의 이메일
   */
  public void deleteRefreshTokenInfo(String email) {
    redisTemplate.delete(makeRefreshTokenKey(email));
  }

  /**
   * Redis key값 생성
   *
   * @param email 유저의 이메일
   * @return Redis 키값
   */
  private String makeRefreshTokenKey(String email) {
    return REFRESH_TOKEN_PREFIX + email;
  }

}
