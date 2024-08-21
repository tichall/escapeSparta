package com.sparta.domain;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consumer")
public class ConsumerStatusController {

  /**
   * 로드밸런서 대상 그룹 상태 검사
   */
  @GetMapping("/status")
  public ResponseEntity<String> getStatus() {
    String statusMessage = "Service is running";
    return new ResponseEntity<>(statusMessage, HttpStatus.OK);
  }
}
