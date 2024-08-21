package com.sparta.global.exception.customException;

import com.sparta.global.exception.errorCode.ErrorCode;

public class PaymentException extends GlobalCustomException {
    public PaymentException(ErrorCode errorCode) {
        super(errorCode);
    }
}