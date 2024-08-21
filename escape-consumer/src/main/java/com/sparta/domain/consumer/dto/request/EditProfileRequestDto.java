package com.sparta.domain.consumer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class EditProfileRequestDto {

    @Pattern(regexp = "^[가-힣]{2,5}$",
            message = "이름 2글자 이상 5글자 이하로 입력해 주세요.")
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
}
