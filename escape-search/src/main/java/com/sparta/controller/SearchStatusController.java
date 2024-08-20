package com.sparta.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class SearchStatusController {

  /**
   * 로드 밸런서 대상그룹 상태 검사 용 api
   */
  @GetMapping("/search/status")
  public ResponseEntity<String> getStatus() {
    String statusMessage = "Service is running";
    return new ResponseEntity<>(statusMessage, HttpStatus.OK);
  }
}