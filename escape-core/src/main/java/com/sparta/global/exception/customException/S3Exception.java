package com.sparta.global.exception.customException;

import com.sparta.global.exception.errorCode.ErrorCode;

public class S3Exception extends GlobalCustomException{
    public S3Exception(ErrorCode errorCode) {
        super(errorCode);
    }
}
