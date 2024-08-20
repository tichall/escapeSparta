package com.sparta.domain.user.controller;

import com.sparta.domain.user.dto.request.CertificateRequestDto;
import com.sparta.domain.user.service.EmailService;
import com.sparta.global.response.ResponseMessage;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core/users/mail")
public class EmailController {

  private final EmailService emailService;

  /**
   * 인증 번호를 이메일로 발송합니다.
   *
   * @param requestDto 인증 요청 정보를 담은 DTO
   * @return 인증 번호가 발송된 이메일 주소에 대한 응답 메시지
   * @throws MessagingException 이메일 발송 중 오류가 발생한 경우
   */
  @PostMapping
  public ResponseEntity<ResponseMessage<String>> sendCertificationNumber(
      @Valid @RequestBody CertificateRequestDto requestDto)
      throws MessagingException {

    String email = emailService.sendEmailForCertification(requestDto);

    ResponseMessage<String> responseMessage = ResponseMessage.<String>builder()
        .statusCode(HttpStatus.OK.value())
        .message("입력한 이메일로 인증코드를 발송했습니다.")
        .data(email)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }
}
