package com.sparta.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ThemeErrorCode implements ErrorCode{
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 방탈출 테마가 존재하지 않습니다."),
    INVALID_THEME_STATUS(HttpStatus.BAD_REQUEST.value(), "헤당 방탈출 테마는 비활성화 상태입니다.");

    private final int httpStatusCode;
    private final String errorDescription;
}
