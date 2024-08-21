package com.sparta.domain.user.dto.response;

import com.sparta.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignupResponseDto {

  private final Long id;
  private final String name;
  private final String email;
  private final Long point; // 일단 넣긴 했는데 필요한지 모르겠음
  private final LocalDateTime createdAt;

  public SignupResponseDto(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.email = user.getEmail();
    this.point = user.getPoint();
    this.createdAt = user.getCreatedAt();
  }

}
