package com.sparta.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReservationErrorCode implements ErrorCode{

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 예약를 찾을 수 없습니다."),
    RESERVATION_DUPLICATION(HttpStatus.NOT_FOUND.value(), "이미 등록된 예약입니다.");

    private final int httpStatusCode;
    private final String errorDescription;
}
