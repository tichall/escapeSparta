package com.sparta.domain.user.service;

import com.sparta.domain.user.dto.request.CertificateRequestDto;
import com.sparta.domain.user.entity.UserType;
import com.sparta.domain.user.repository.EmailRepository;
import com.sparta.global.exception.customException.EmailException;
import com.sparta.global.exception.errorCode.EmailErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;
  private final EmailRepository emailRepository;

  public static final String MAIL_TITLE_CERTIFICATION = "이메일 인증입니다";

  /**
   * 인증 번호를 이메일로 발송합니다.
   *
   * @param requestDto 인증 요청 정보를 담은 DTO
   * @return 인증 번호가 발송된 이메일 주소
   * @throws MessagingException 이메일 발송 중 오류가 발생한 경우
   */
  public String sendEmailForCertification(CertificateRequestDto requestDto)
      throws MessagingException {
    String email = requestDto.getEmail();
    String certificationNumber = createCertificateCode(requestDto.getUserType());

    String content = String.format("인증 코드 : " + certificationNumber + "\n인증 코드를 5분 이내에 입력해주세요.");
    emailRepository.saveCertificationNumber(email, certificationNumber);
    sendMail(email, content);
    return email;
  }

  /**
   * 이메일로 인증 번호를 발송합니다.
   *
   * @param email   수신자 이메일 주소
   * @param content 이메일 내용
   * @throws MessagingException 이메일 발송 중 오류가 발생한 경우
   */
  private void sendMail(String email, String content) throws MessagingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
    helper.setTo(email);
    helper.setSubject(MAIL_TITLE_CERTIFICATION);
    helper.setText(content);
    mailSender.send(mimeMessage);
  }

  /**
   * 이메일 인증 번호를 검증합니다.
   *
   * @param email               이메일 주소
   * @param certificationNumber 인증 번호
   * @throws EmailException 인증 번호가 일치하지 않는 경우
   */
  public void verifyEmail(String email, String certificationNumber) {
    if (!emailRepository.getCertificationNumber(email).equals(certificationNumber)) {
      throw new EmailException(EmailErrorCode.EMAIL_VERIFICATION_CODE_MISMATCH);
    }
  }

  /**
   * 인증 번호를 생성합니다.
   *
   * @param userType 유저 타입에 따라 인증번호 생성
   * @return 생성된 인증 번호
   */
  public String createCertificateCode(UserType userType) {
    String prefix = switch (userType) {
      case ADMIN -> "1";
      case MANAGER -> "2";
      default -> "0";
    };
    SecureRandom secureRandom = new SecureRandom();
    int randomNumber = secureRandom.nextInt(900000) + 10000; // 100000 ~ 999999 범위의 6자리 난수 생성
    return prefix + randomNumber;
  }

  /**
   * 인증 번호를 기반으로 유저 타입을 결정합니다.
   *
   * @param code 인증 번호
   * @return 유저 타입
   */
  public UserType determineUserTypeFromCertificateCode(String code) {
    String prefix = code.substring(0, 1);
    return switch (prefix) {
      case "1" -> UserType.ADMIN;
      case "2" -> UserType.MANAGER;
      default -> UserType.USER;
    };
  }
}