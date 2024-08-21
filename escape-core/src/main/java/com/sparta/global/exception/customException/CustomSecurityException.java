package com.sparta.global.exception.customException;

import com.sparta.global.exception.errorCode.ErrorCode;

public class CustomSecurityException extends GlobalCustomException{
    public CustomSecurityException(ErrorCode errorCode) {
        super(errorCode);
    }
}
