package com.sparta.global.exception.customException;

import com.sparta.global.exception.errorCode.ErrorCode;

public class AuthException extends GlobalCustomException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
