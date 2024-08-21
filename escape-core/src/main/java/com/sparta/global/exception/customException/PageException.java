package com.sparta.global.exception.customException;

import com.sparta.global.exception.errorCode.ErrorCode;

public class PageException extends GlobalCustomException{
    public PageException(ErrorCode errorCode){
        super(errorCode);
    }
}
