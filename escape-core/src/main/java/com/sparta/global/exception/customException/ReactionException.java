package com.sparta.global.exception.customException;

import com.sparta.global.exception.errorCode.ErrorCode;

public class ReactionException extends GlobalCustomException {
    public ReactionException(ErrorCode errorCode) {
        super(errorCode);
    }
}