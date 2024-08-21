package com.sparta.global.exception.customException;

import com.sparta.global.exception.errorCode.ErrorCode;

public class LocalDateTimeException extends GlobalCustomException{
    public LocalDateTimeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
