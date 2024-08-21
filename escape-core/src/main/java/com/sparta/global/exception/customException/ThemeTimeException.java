package com.sparta.global.exception.customException;

import com.sparta.global.exception.errorCode.ErrorCode;
import lombok.Getter;

@Getter
public class ThemeTimeException extends GlobalCustomException{
    public ThemeTimeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
