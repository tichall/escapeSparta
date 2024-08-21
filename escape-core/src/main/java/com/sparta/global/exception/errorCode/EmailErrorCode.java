package com.sparta.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EmailErrorCode implements ErrorCode{

    VERIFY_NOT_ALLOWED(HttpStatus.UNAUTHORIZED.value(),"이메일 인증이 필요합니다."),
    EMAIL_VERIFICATION_CODE_MISMATCH(HttpStatus.UNAUTHORIZED.value(),"인증 코드가 일치하지 않습니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST.value(), "인증 코드 재요청이 필요합니다.");


    private final int httpStatusCode;
    private final String errorDescription;
}
