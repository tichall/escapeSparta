package com.sparta.global.exception.customException;

import com.sparta.global.exception.errorCode.ErrorCode;

public class RecommendationException extends GlobalCustomException {
    public RecommendationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
