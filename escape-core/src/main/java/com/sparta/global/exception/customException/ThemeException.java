package com.sparta.global.exception.customException;

import com.sparta.global.exception.errorCode.ErrorCode;

public class ThemeException extends GlobalCustomException{
    public ThemeException(ErrorCode errorCode) {
        super(errorCode);
    }
}