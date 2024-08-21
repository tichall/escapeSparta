package com.sparta.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements ErrorCode{
    FILE_MAX_SIZE_ERROR(HttpStatus.BAD_REQUEST.value(), "첨부파일의 크기는 2MB를 넘을 수 없습니다."),
    IMAGE_STREAM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미지 업로드 중 오류가 발생했습니다."),
    INVALID_EXTENSION(HttpStatus.BAD_REQUEST.value(), "첨부할 수 없는 파일 확장자입니다."),
    INVALID_IMAGE_URL(HttpStatus.BAD_REQUEST.value(), "잘못된 이미지 URL입니다.");

    private final int httpStatusCode;
    private final String errorDescription;
}
