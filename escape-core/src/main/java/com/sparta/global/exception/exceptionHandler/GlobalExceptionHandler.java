package com.sparta.global.exception.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.global.exception.customException.GlobalCustomException;
import com.sparta.global.exception.errorCode.CommonErrorCode;
import com.sparta.global.exception.errorCode.S3ErrorCode;
import com.sparta.global.response.ResponseErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Validation 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ResponseErrorMessage<String>> handleValidException(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException] caused By : {}, message : {} ", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());

        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (existingValue, newValue) -> existingValue
                ));

        ObjectMapper mapper = new ObjectMapper();
        String response = "";
        try {
            response = mapper.writeValueAsString(errors);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ResponseErrorMessage<String> errorMessage = new ResponseErrorMessage<>(CommonErrorCode.BAD_REQUEST, response);

        return ResponseEntity.status(CommonErrorCode.BAD_REQUEST.getHttpStatusCode())
                .body(errorMessage);
    }

    /**
     * 파일 크기 초과 예외 처리
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<ResponseErrorMessage<Void>> maxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("MaxUploadSizeExceededException 발생");
        return ResponseEntity.status(S3ErrorCode.FILE_MAX_SIZE_ERROR.getHttpStatusCode())
                .body(new ResponseErrorMessage<>(S3ErrorCode.FILE_MAX_SIZE_ERROR));
    }

    /**
     * CustomException 예외 처리
     */
    @ExceptionHandler(GlobalCustomException.class)
    protected ResponseEntity<ResponseErrorMessage<Void>> handlerGlobalCustomException(GlobalCustomException e) {
        log.error("{} 예외 발생", e.getClass());

        return ResponseEntity.status(e.getErrorCode().getHttpStatusCode())
                .body(new ResponseErrorMessage<>(e.getErrorCode()));
    }
}
