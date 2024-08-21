package com.sparta.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LocalDateTimeErrorCode implements ErrorCode{
    DATETIME_PARSE_ERROR(HttpStatus.BAD_REQUEST.value(), "잘못된 날짜 형식입니다."),
    INVALID_PAST_TIME(HttpStatus.BAD_REQUEST.value(), "이미 지난 시간대는 등록할 수 없습니다."),
    INVALID_START_AND_END_TIME(HttpStatus.BAD_REQUEST.value(), "종료 시간이 시작 시간보다 빠를 수 없습니다.");

    private final int httpStatusCode;
    private final String errorDescription;
}
