package com.sparta.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseMessage<T> {
    private final int statusCode;
    private final String message;

    // null인 경우 응답 객체에 포함되지 않음
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    @Builder
    public ResponseMessage(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }
}
