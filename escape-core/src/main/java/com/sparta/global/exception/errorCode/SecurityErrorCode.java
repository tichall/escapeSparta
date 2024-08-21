package com.sparta.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SecurityErrorCode implements ErrorCode {
    LOGIN_FAILED(HttpStatus.BAD_REQUEST.value(), "로그인에 실패했습니다."),
    WITHDRAWAL_USER(HttpStatus.BAD_REQUEST.value(), "탈퇴한 회원입니다."),
    USER_FORBIDDEN(HttpStatus.FORBIDDEN.value(), "접근 권한이 없습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "만료된 액세스 토큰입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 토큰입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 액세스 토큰입니다.");

    private final int httpStatusCode;
    private final String errorDescription;
}
