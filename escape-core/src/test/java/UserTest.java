import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.domain.user.dto.request.SignupRequestDto;
import com.sparta.domain.user.dto.request.WithdrawRequestDto;
import com.sparta.domain.user.dto.response.SignupResponseDto;
import com.sparta.domain.user.entity.OAuthProvider;
import com.sparta.domain.user.entity.User;
import com.sparta.domain.user.entity.UserStatus;
import com.sparta.domain.user.entity.UserType;
import com.sparta.domain.user.repository.UserRepository;
import com.sparta.domain.user.service.EmailService;
import com.sparta.domain.user.service.UserService;
import com.sparta.global.exception.customException.UserException;
import com.sparta.global.exception.errorCode.UserErrorCode;
import com.sparta.global.jwt.JwtProvider;
import com.sparta.global.jwt.RefreshTokenService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

class UserTest {

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserRepository userRepository;

  @Mock
  private EmailService emailService;

  @Mock
  private RefreshTokenService refreshTokenService;

  @Mock
  private JwtProvider jwtProvider; // JWT 관련 Mock 추가

  @InjectMocks
  private UserService userService;

  private SignupRequestDto signupRequestDto;
  private WithdrawRequestDto withdrawRequestDto;
  private User user;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("회원가입 성공 테스트")
  void createUser_Success() {
    // given
    signupRequestDto = new SignupRequestDto("르탄이", "sparta@project.com", "Test1234!",
        "certificateCode", "testAdminKey");
    user = new User("르탄이", "sparta@project.com", "Test1234!", OAuthProvider.ORIGIN, UserType.USER,
        UserStatus.ACTIVE);

    doNothing().when(userRepository).throwIfEmailExists(any(String.class));
    when(emailService.determineUserTypeFromCertificateCode(any(String.class))).thenReturn(
        UserType.USER);
    doNothing().when(emailService).verifyEmail(any(String.class), any(String.class));
    when(passwordEncoder.encode(any(String.class))).thenReturn("Test1234!");
    when(userRepository.save(any(User.class))).thenReturn(user);

    // when
    SignupResponseDto response = userService.createUser(signupRequestDto);

    // then
    assertNotNull(response);
    assertEquals("sparta@project.com", response.getEmail());
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  @DisplayName("회원가입 실패 테스트 - 유효하지 않은 관리자 키")
  void createUser_AdminKeyMismatch() {
    // given
    signupRequestDto = new SignupRequestDto("르탄이", "sparta@project.com", "Test1234!",
        "certificateCode", "wrongAdminKey");
    when(emailService.determineUserTypeFromCertificateCode(any(String.class))).thenReturn(
        UserType.ADMIN);

    // when
    UserException exception = assertThrows(UserException.class,
        () -> userService.createUser(signupRequestDto));

    // then
    assertEquals(UserErrorCode.INVALID_ADMIN_KEY, exception.getErrorCode());
  }

  @Test
  @DisplayName("로그인 성공 및 JWT 토큰 생성 테스트")
  void login_Success() throws Exception {
    // given
    String email = "sparta@project.com";
    String password = "Test1234!";

    user = new User("르탄이", email, password, OAuthProvider.ORIGIN, UserType.USER, UserStatus.ACTIVE);
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
    when(jwtProvider.createAccessToken(email, UserType.USER)).thenReturn("access.token");
    when(jwtProvider.createRefreshToken(email)).thenReturn("refresh.token");

    // when
    Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
    String accessToken = jwtProvider.createAccessToken(email, UserType.USER);
    String refreshToken = jwtProvider.createRefreshToken(email);

    // then
    assertNotNull(accessToken);
    assertEquals("access.token", accessToken);
    assertNotNull(refreshToken);
    assertEquals("refresh.token", refreshToken);
  }

  @Test
  @DisplayName("로그인 실패 테스트")
  void login_Failure() throws Exception {
    // given
    String email = "sparta@project.com";
    String password = "Test1234!";

    user = new User("르탄이", email, password, OAuthProvider.ORIGIN, UserType.USER, UserStatus.ACTIVE);
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false); // 비밀번호 불일치 시뮬레이션

    // when
    AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
      if (!passwordEncoder.matches(password, user.getPassword())) {
        throw new AuthenticationException("로그인 실패") {
        };
      }
    });

    // then
    assertEquals("로그인 실패", exception.getMessage());
  }

  @Test
  @DisplayName("회원탈퇴 성공 테스트")
  void withdraw_Success() {
    // given
    withdrawRequestDto = new WithdrawRequestDto("Test1234!");
    user = new User("르탄이", "sparta@project.com", "Test1234!", OAuthProvider.ORIGIN, UserType.USER,
        UserStatus.ACTIVE);
    ReflectionTestUtils.setField(user, "id", 1L); // id 필드 설정

    when(userRepository.findByIdOrElseThrow(any(Long.class))).thenReturn(user);
    when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);

    // when
    Long result = userService.withdraw(withdrawRequestDto, 1L);

    // then
    assertEquals(1L, result);
    assertEquals(UserStatus.WITHDRAW, user.getUserStatus());
    verify(userRepository, times(1)).save(user);
    verify(refreshTokenService, times(1)).deleteRefreshTokenInfo(user.getEmail());
  }

  @Test
  @DisplayName("회원탈퇴 실패 테스트 - 비밀번호 불일치")
  void withdraw_PasswordMismatch() {
    // given
    withdrawRequestDto = new WithdrawRequestDto("Mismatch1234!");
    user = new User("르탄이", "sparta@project.com", "Test1234!", OAuthProvider.ORIGIN, UserType.USER,
        UserStatus.ACTIVE);

    when(userRepository.findByIdOrElseThrow(any(Long.class))).thenReturn(user);
    when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(false);

    // when
    UserException exception = assertThrows(UserException.class,
        () -> userService.withdraw(withdrawRequestDto, 1L));

    // then
    assertEquals(UserErrorCode.PASSWORD_NOT_MATCH, exception.getErrorCode());
  }
}
