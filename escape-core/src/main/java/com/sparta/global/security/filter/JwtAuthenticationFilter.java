package com.sparta.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.domain.user.dto.request.LoginRequestDto;
import com.sparta.domain.user.entity.User;
import com.sparta.domain.user.entity.UserStatus;
import com.sparta.domain.user.entity.UserType;
import com.sparta.global.exception.errorCode.SecurityErrorCode;
import com.sparta.global.jwt.JwtProvider;
import com.sparta.global.jwt.RefreshTokenService;
import com.sparta.global.security.UserDetailsImpl;
import com.sparta.global.util.ResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final JwtProvider jwtProvider;

  private final RefreshTokenService refreshTokenService;

  public JwtAuthenticationFilter(JwtProvider jwtProvider, RefreshTokenService refreshTokenService) {
    this.jwtProvider = jwtProvider;
    this.refreshTokenService = refreshTokenService;
    setFilterProcessesUrl("/api/core/users/login");
  }

  /**
   * 로그인 시도
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    try {
      LoginRequestDto requestDto = new ObjectMapper()
          .readValue(request.getInputStream(), LoginRequestDto.class);

      return getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken(
              requestDto.getEmail(),
              requestDto.getPassword(),
              null
          )
      );
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * 로그인 성공 및 JWT 생성
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException {
    UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
    User loginUser = userDetails.getUser();

    if (loginUser.getUserStatus() == UserStatus.ACTIVE) {
      UserType role = loginUser.getUserType();

      String userEmail = loginUser.getEmail();
      String accessToken = jwtProvider.createAccessToken(userEmail, role);
      String refreshToken = jwtProvider.createRefreshToken(userEmail);

      refreshTokenService.saveRefreshTokenInfo(userEmail, refreshToken);

      // 응답 헤더에 토큰 추가
      response.addHeader(JwtProvider.AUTHORIZATION_HEADER, accessToken);
      response.addHeader(JwtProvider.REFRESH_HEADER, refreshToken);

      // JSON 응답 작성
      ResponseUtil.writeJsonResponse(response, HttpStatus.OK, "로그인에 성공했습니다.", authResult.getName());

      log.debug("User = {}, message = {}", userEmail, "로그인에 성공했습니다.");
    } else {
      ResponseUtil.writeJsonErrorResponse(response, SecurityErrorCode.WITHDRAWAL_USER);
    }
  }

  /**
   * 로그인 실패
   */
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed) throws IOException {
    log.debug("로그인 실패 : {}", failed.getMessage());
    ResponseUtil.writeJsonErrorResponse(response, SecurityErrorCode.LOGIN_FAILED);
  }

}
