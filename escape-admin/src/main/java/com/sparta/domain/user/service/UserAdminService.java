package com.sparta.domain.user.service;

import com.sparta.domain.user.dto.response.UserResponseDto;
import com.sparta.domain.user.entity.User;
import com.sparta.domain.user.entity.UserType;
import com.sparta.domain.user.repository.UserRepository;
import com.sparta.global.exception.customException.UserException;
import com.sparta.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAdminService {

  private final UserRepository userRepository;

  /**
   * 모든 Manager 정보 조회
   *
   * @return Manager 정보 리스트
   */
  @Transactional(readOnly = true)
  public List<UserResponseDto> getAllManagers() {

    List<User> managers = getUsers(UserType.MANAGER);

    return managers.stream()
        .map(UserResponseDto::new)
        .collect(Collectors.toList());
  }

  /**
   * 모든 Consumer 정보 조회
   *
   * @return Consumer 정보 리스트
   */
  @Transactional(readOnly = true)
  public List<UserResponseDto> getAllConsumers() {
    List<User> consumers = getUsers(UserType.USER);

    return consumers.stream()
        .map(UserResponseDto::new)
        .collect(Collectors.toList());
  }


  /* Utils */

  /**
   * UserType에 맞는 모든 유저 찾기
   *
   * @param userType 유저 타입
   * @return 유저 리스트
   */
  private List<User> getUsers(UserType userType) {
    List<User> users = userRepository.findByUserType(userType);

    if (users.isEmpty()) {
      throw new UserException(UserErrorCode.USER_NO_ONE);
    }

    return users;
  }

}
