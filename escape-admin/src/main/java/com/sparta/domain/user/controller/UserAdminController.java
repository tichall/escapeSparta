package com.sparta.domain.user.controller;

import com.sparta.domain.user.dto.response.UserResponseDto;
import com.sparta.domain.user.service.UserAdminService;
import com.sparta.global.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class UserAdminController {

  private final UserAdminService userAdminService;

  /**
   * 모든 Manger 정보 조회
   *
   * @return status.code, message, Manger 정보 리스트 반환
   */
  @GetMapping("/managers")
  public ResponseEntity<ResponseMessage<List<UserResponseDto>>> getAllManagers() {

    List<UserResponseDto> responseDto = userAdminService.getAllManagers();

    ResponseMessage<List<UserResponseDto>> responseMessage = ResponseMessage.<List<UserResponseDto>>builder()
        .statusCode(HttpStatus.OK.value())
        .data(responseDto)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 모든 Consumer 정보 조회
   *
   * @return status.code, message, Consumer 정보 리스트 반환
   */
  @GetMapping("/consumers")
  public ResponseEntity<ResponseMessage<List<UserResponseDto>>> getAllConsumers() {

    List<UserResponseDto> responseDto = userAdminService.getAllConsumers();

    ResponseMessage<List<UserResponseDto>> responseMessage = ResponseMessage.<List<UserResponseDto>>builder()
        .statusCode(HttpStatus.OK.value())
        .data(responseDto)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }
}
