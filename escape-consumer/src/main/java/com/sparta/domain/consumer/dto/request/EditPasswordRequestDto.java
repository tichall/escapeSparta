package com.sparta.domain.consumer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class EditPasswordRequestDto {

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[a-zA-Z0-9]).{7,}$",
            message = "비밀번호는 대문자 1개, 특수문자 1개, 영문 및 숫자를 포함한 7자 이상이어야 합니다.")
    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[a-zA-Z0-9]).{7,}$",
            message = "비밀번호는 대문자 1개, 특수문자 1개, 영문 및 숫자를 포함한 7자 이상이어야 합니다.")
    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    private String newPassword;
}
