package com.sparta.domain.theme.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ThemeTimeCreateRequestDto {
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$", message = "YYYY-MM-DD HH:mm 형식으로 입력해주세요.")
    private String startTime;
}
