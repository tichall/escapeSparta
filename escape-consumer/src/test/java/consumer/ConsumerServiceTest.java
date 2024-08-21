package consumer;

import com.sparta.domain.consumer.dto.request.EditPasswordRequestDto;
import com.sparta.domain.consumer.dto.request.EditProfileRequestDto;
import com.sparta.domain.consumer.service.ConsumerService;
import com.sparta.domain.user.dto.response.UserResponseDto;
import com.sparta.domain.user.entity.OAuthProvider;
import com.sparta.domain.user.entity.User;
import com.sparta.domain.user.entity.UserStatus;
import com.sparta.domain.user.entity.UserType;
import com.sparta.domain.user.repository.UserRepository;
import com.sparta.global.exception.customException.GlobalCustomException;
import com.sparta.global.exception.customException.UserException;
import com.sparta.global.exception.errorCode.UserErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConsumerServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ConsumerService consumerService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("박성균", "yooss135@naver.com", "Test1234!", OAuthProvider.ORIGIN, UserType.USER, UserStatus.ACTIVE);
    }

    @Test
    public void 프로필_수정() {
        User user = mock(User.class);
        EditProfileRequestDto request = mock(EditProfileRequestDto.class);
        when(request.getName()).thenReturn("newName");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        UserResponseDto responseDto = consumerService.modifyProfile(request, user);

        // then
        assertThat(responseDto.getName()).isEqualTo(user.getName());
    }

    @Test
    public void 비밀번호_수정_성공() {
        // given
        User user = mock(User.class);
        EditPasswordRequestDto request = mock(EditPasswordRequestDto.class);
        when(request.getCurrentPassword()).thenReturn("currentPassword");
        when(request.getNewPassword()).thenReturn("newPassword");
        when(passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(request.getNewPassword(), user.getPassword())).thenReturn(false);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        UserResponseDto responseDto = consumerService.editPassword(request, user);

        // then
        assertThat(responseDto).isNotNull();
        verify(user).changePassword("encodedNewPassword");
    }

    @Test
    public void 비밀번호_수정_실패_패스워드_틀림() {
        // given
        User user = mock(User.class);
        EditPasswordRequestDto request = mock(EditPasswordRequestDto.class);
        when(request.getCurrentPassword()).thenReturn("currentPassword");
        when(passwordEncoder.matches("currentPassword", user.getPassword())).thenReturn(false);

        // when
        GlobalCustomException e = assertThrows(UserException.class, () -> consumerService.editPassword(request, user));

        //then
        assertThat(e.getErrorCode()).isEqualTo(UserErrorCode.PASSWORD_NOT_MATCH);

    }

    @Test
    public void 비밀번호_수정_실패_동일한_비밀번호() {
        // given
        User user = mock(User.class);
        EditPasswordRequestDto request = mock(EditPasswordRequestDto.class);
        when(request.getCurrentPassword()).thenReturn("currentPassword");
        when(request.getNewPassword()).thenReturn("newPassword");
        when(passwordEncoder.matches("currentPassword", user.getPassword())).thenReturn(true);
        when(passwordEncoder.matches("newPassword", user.getPassword())).thenReturn(true);

        // when
        GlobalCustomException e = assertThrows(UserException.class, () -> consumerService.editPassword(request, user));

        //then
        assertThat(e.getErrorCode()).isEqualTo(UserErrorCode.PASSWORD_NOT_MIXMATCH);
    }

}
