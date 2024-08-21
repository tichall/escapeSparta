package com.sparta.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.global.exception.errorCode.ErrorCode;
import com.sparta.global.response.ResponseErrorMessage;
import com.sparta.global.response.ResponseMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ResponseUtil {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * HttpResponse에 JSON 형태로 응답하기
   */
  public static void writeJsonResponse(HttpServletResponse response, HttpStatus status,
      String message, String data) throws IOException {
    ResponseMessage<String> responseMessage = ResponseMessage.<String>builder()
        .statusCode(status.value())
        .message(message)
        .data(data)
        .build();

    String jsonResponse = objectMapper.writeValueAsString(responseMessage);
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=UTF-8");
    response.getWriter().write(jsonResponse);
  }

  /**
   * HttpResponse에 JSON 형태로 응답하기
   */
  public static void writeJsonErrorResponse(HttpServletResponse response, ErrorCode errorCode)
      throws IOException {
    ResponseErrorMessage<Void> responseMessage = new ResponseErrorMessage<Void>(errorCode);

    String jsonResponse = objectMapper.writeValueAsString(responseMessage);
    
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.setContentType(
        MediaType.APPLICATION_JSON_VALUE + "; charset=" + StandardCharsets.UTF_8.name());
    response.setStatus(errorCode.getHttpStatusCode());
    response.getWriter().write(jsonResponse);
  }
}
