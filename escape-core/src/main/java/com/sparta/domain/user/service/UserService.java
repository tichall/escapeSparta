package com.sparta.domain.user.service;

import com.sparta.domain.user.dto.request.SignupRequestDto;
import com.sparta.domain.user.dto.request.WithdrawRequestDto;
import com.sparta.domain.user.dto.response.SignupResponseDto;
import com.sparta.domain.user.entity.OAuthProvider;
import com.sparta.domain.user.entity.User;
import com.sparta.domain.user.entity.UserStatus;
import com.sparta.domain.user.entity.UserType;
import com.sparta.domain.user.repository.UserRepository;
import com.sparta.global.exception.customException.UserException;
import com.sparta.global.exception.errorCode.UserErrorCode;
import com.sparta.global.jwt.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final EmailService emailService;
  private final RefreshTokenService refreshTokenService;

  @Value("${admin.key}")
  private String adminKey;

  /**
   * 회원가입 메서드
   *
   * @param requestDto 회원가입 요청 데이터를 담은 Dto
   * @return 회원가입된 사용자의 정보를 담은 Dto
   * @throws UserException 유저 중복 예외처리
   */
  @Transactional
  public SignupResponseDto createUser(SignupRequestDto requestDto) {

    // 이메일로 유저 중복검사
    userRepository.throwIfEmailExists(requestDto.getEmail());

    // 인증 코드로 회원가입할 Role 체크
    UserType userType = emailService.determineUserTypeFromCertificateCode(
        requestDto.getCertificateCode());

    // 이메일 인증
    emailService.verifyEmail(requestDto.getEmail(), requestDto.getCertificateCode());

    if (userType == UserType.ADMIN) {
      if (!requestDto.getAdminKey().equals(adminKey)) {
        throw new UserException(UserErrorCode.INVALID_ADMIN_KEY);
      }
    }

    String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

    User user = new User(
        requestDto.getName(),
        requestDto.getEmail(),
        encodedPassword,
        OAuthProvider.ORIGIN,
        userType,
        UserStatus.ACTIVE
    );

    userRepository.save(user);
    return new SignupResponseDto(user);
  }

  /**
   * 로그아웃 메서드
   *
   * @param userId 사용자 ID
   * @return 로그아웃된 사용자 ID
   */
  @Transactional(readOnly = true)
  public Long logout(Long userId) {

    User user = userRepository.findByIdOrElseThrow(userId);
    refreshTokenService.deleteRefreshTokenInfo(user.getEmail());

    return user.getId();
  }

  /**
   * 회원 탈퇴 메서드
   *
   * @param requestDto 회원 탈퇴 요청 데이터를 담은 DTO
   * @param userId     사용자 ID
   * @return 탈퇴된 사용자 ID
   * @throws UserException 비밀번호 불일치 예외처리
   */
  @Transactional
  public Long withdraw(WithdrawRequestDto requestDto, Long userId) {

    User user = userRepository.findByIdOrElseThrow(userId);

    if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
      throw new UserException(UserErrorCode.PASSWORD_NOT_MATCH);
    }

    user.changeStatus(UserStatus.WITHDRAW);
    userRepository.save(user);
    refreshTokenService.deleteRefreshTokenInfo(user.getEmail());

    return user.getId();
  }
}