package com.sparta.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDto {

  @Pattern(regexp = "^[가-힣]{2,5}$",
      message = "이름 2글자 이상 5글자 이하로 입력해 주세요.")
  @NotBlank(message = "이름은 필수 입력 값입니다.")
  private String name;

  @Email(message = "이메일 형식이 아닙니다.")
  @NotBlank(message = "이메일은 필수 입력 값입니다.")
  private String email;

  @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[a-zA-Z0-9]).{7,}$", // @Size로 길이제한 고려
      message = "비밀번호는 대문자 1개, 특수문자 1개, 영문 및 숫자를 포함한 7자 이상이어야 합니다.")
  @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
  private String password;

  @NotBlank(message = "인증 코드는 필수 입력 값입니다.")
  private String certificateCode;

  private String adminKey;


  public SignupRequestDto(String name, String email, String password, String certificateCode,
      String adminKey) {

    this.name = name;
    this.email = email;
    this.password = password;
    this.certificateCode = certificateCode;
    this.adminKey = adminKey;
  }
}
