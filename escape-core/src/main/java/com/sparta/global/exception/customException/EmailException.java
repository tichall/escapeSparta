package com.sparta.global.exception.customException;

import com.sparta.global.exception.errorCode.ErrorCode;

public class EmailException extends GlobalCustomException {
    public EmailException(ErrorCode errorCode) {super(errorCode);}
}
