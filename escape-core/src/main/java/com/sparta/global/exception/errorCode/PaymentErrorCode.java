package com.sparta.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode implements ErrorCode {
    PAMENT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "결제정보를 찾을 수 없습니다."),
    ALREAY_REFUND(HttpStatus.FORBIDDEN.value(), "이미 환불 된 결제입니다.");

    private final int httpStatusCode;
    private final String errorDescription;
}
