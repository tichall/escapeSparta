package com.sparta.domain.user.dto.request;

import com.sparta.domain.user.entity.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CertificateRequestDto {

    @NotBlank(message = "인증 코드를 받을 이메일을 입력해주세요.")
    private String email;

    @NotNull(message = "USER, ADMIN, MANAGER 중 한가지를 입력해주세요.")
    private UserType userType;
}
