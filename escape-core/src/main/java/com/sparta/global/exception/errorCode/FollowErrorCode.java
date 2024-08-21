package com.sparta.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FollowErrorCode implements ErrorCode{
    FOLLOW_DUPLICATION(HttpStatus.BAD_REQUEST.value(), "이미 등록된 팔로워입니다."),
    FOLLOW_NOT_STORE(HttpStatus.NOT_FOUND.value(), "팔로우 되지 않은 스토어 입니다."),
    ;
    private final int httpStatusCode;
    private final String errorDescription;
}
