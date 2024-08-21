package com.sparta.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RecommendationErrorCode implements ErrorCode{
    RECOMMENDATION_DUPLICATION(HttpStatus.NOT_FOUND.value(), "이미 등록된 좋아요입니다."),
    RECOMMENDATION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "좋아요를 찾을 수 없습니다.");

    private final int httpStatusCode;
    private final String errorDescription;
}
