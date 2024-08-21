package com.sparta.global.security.filter;

import com.sparta.global.exception.customException.CustomSecurityException;
import com.sparta.global.exception.errorCode.CommonErrorCode;
import com.sparta.global.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j(topic = "인증 예외 처리")
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Exception exception = (Exception) request.getAttribute("exception");

        if (exception instanceof CustomSecurityException e) {
            ResponseUtil.writeJsonErrorResponse(response, e.getErrorCode());
            return;
        }

        ResponseUtil.writeJsonErrorResponse(response, CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
}
