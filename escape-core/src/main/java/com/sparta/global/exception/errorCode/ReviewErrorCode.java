package com.sparta.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode{
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 리뷰를 찾을 수 없습니다."),
    REVIEW_DUPLICATION(HttpStatus.BAD_REQUEST.value(), "이미 등록된 리뷰입니다."),
    REVIEW_NOT_REACTION(HttpStatus.BAD_REQUEST.value(), "자신이 등록한 리뷰에는 리액션을 할 수 없습니다.")
    ;

    private final int httpStatusCode;
    private final String errorDescription;
}