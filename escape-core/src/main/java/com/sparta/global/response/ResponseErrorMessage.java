package com.sparta.global.response;

import com.sparta.global.exception.errorCode.ErrorCode;
import lombok.Getter;

@Getter
public class ResponseErrorMessage<T> extends ResponseMessage<T> {

    public ResponseErrorMessage(ErrorCode errorCode) {
        super(errorCode.getHttpStatusCode(), errorCode.getErrorDescription(), null);
    }

    public ResponseErrorMessage(ErrorCode errorCode, T data) {
        super(errorCode.getHttpStatusCode(), errorCode.getErrorDescription(), data);
    }
}