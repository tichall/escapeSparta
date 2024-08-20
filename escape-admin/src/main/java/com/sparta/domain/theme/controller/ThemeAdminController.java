package com.sparta.domain.theme.controller;

import com.sparta.domain.theme.dto.request.ThemeCreateRequestDto;
import com.sparta.domain.theme.dto.request.ThemeModifyRequestDto;
import com.sparta.domain.theme.dto.response.ThemeDetailResponseDto;
import com.sparta.domain.theme.dto.response.ThemeGetResponseDto;
import com.sparta.domain.theme.service.ThemeAdminService;
import com.sparta.global.response.ResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/stores")
@RequiredArgsConstructor
public class ThemeAdminController {

  private final ThemeAdminService themeService;

  /**
   * 방탈출 테마 등록
   *
   * @param file       테마 이미지 파일
   * @param requestDto 테마 정보 Dto
   * @return status.code, message, 등록한 테마 정보 반환
   */
  @PostMapping("/themes")
  public ResponseEntity<ResponseMessage<ThemeDetailResponseDto>> createTheme(
      @RequestPart(value = "file", required = false) MultipartFile file,
      @Valid @RequestPart ThemeCreateRequestDto requestDto
  ) {
    ThemeDetailResponseDto responseDto = themeService.createTheme(file, requestDto);

    ResponseMessage<ThemeDetailResponseDto> responseMessage = ResponseMessage.<ThemeDetailResponseDto>builder()
        .statusCode(HttpStatus.CREATED.value())
        .message("방탈출 테마 등록이 완료되었습니다.")
        .data(responseDto)
        .build();

    return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
  }

  /**
   * 방탈출 카페의 전체 테마 조회
   *
   * @param storeId 카페 id
   * @return status.code, message, 테마 리스트 반환
   */
  @GetMapping("/{storeId}/themes")
  public ResponseEntity<ResponseMessage<ThemeGetResponseDto>> getThemes(
      @PathVariable Long storeId
  ) {
    ThemeGetResponseDto responseDto = themeService.getThemes(storeId);

    ResponseMessage<ThemeGetResponseDto> responseMessage = ResponseMessage.<ThemeGetResponseDto>builder()
        .statusCode(HttpStatus.OK.value())
        .message("해당 방탈출 카페의 모든 테마 조회가 완료되었습니다.")
        .data(responseDto)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 방탈출 테마 수정
   *
   * @param themeId    테마 id
   * @param requestDto 수정할 테마 정보 Dto
   * @return status.code, message, 수정한 테마 정보 반환
   */
  @PutMapping("/themes/{themeId}")
  public ResponseEntity<ResponseMessage<ThemeDetailResponseDto>> modifyTheme(
      @PathVariable Long themeId,
      @Valid @RequestBody ThemeModifyRequestDto requestDto
  ) {
    ThemeDetailResponseDto responseDto = themeService.modifyTheme(themeId, requestDto);

    ResponseMessage<ThemeDetailResponseDto> responseMessage = ResponseMessage.<ThemeDetailResponseDto>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출 테마 수정이 완료되었습니다.")
        .data(responseDto)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 방탈출 테마 이미지 수정
   *
   * @param themeId 테마 id
   * @param file    수정할 이미지 파일
   * @return status.code, message, 수정한 이미지 경로 반환
   */
  @PutMapping("/themes/{themeId}/image")
  public ResponseEntity<ResponseMessage<String>> modifyThemeImage(
      @PathVariable Long themeId,
      @RequestPart(value = "file", required = false) MultipartFile file
  ) {
    String imagePath = themeService.modifyThemeImage(themeId, file);

    ResponseMessage<String> responseMessage = ResponseMessage.<String>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출 테마 이미지 수정이 완료되었습니다.")
        .data(imagePath)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 방탈출 테마 이미지 삭제
   *
   * @param themeId 테마 id
   * @return status.code, message
   */
  @DeleteMapping("/themes/{themeId}/image")
  public ResponseEntity<ResponseMessage<Void>> deleteThemeImage(
      @PathVariable Long themeId
  ) {
    themeService.deleteThemeImage(themeId);

    ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출 테마 이미지 삭제가 완료되었습니다.")
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 방탈출 테마 상태 변경
   *
   * @param themeId 테마 id
   * @return status.code, message
   */
  @PutMapping("themes/{themeId}/status")
  public ResponseEntity<ResponseMessage<Void>> changeThemeStatus(
      @PathVariable Long themeId
  ) {
    themeService.changeThemeStatus(themeId);

    ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출 테마 상태 변경이 완료되었습니다.")
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 방탈출 테마 완전히 삭제
   *
   * @param themeId 테마 id
   * @return status.code, message
   */
  @DeleteMapping("/themes/{themeId}")
  public ResponseEntity<ResponseMessage<Void>> deleteTheme(
      @PathVariable Long themeId
  ) {
    themeService.deleteTheme(themeId);

    ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출 테마 삭제가 완료되었습니다.")
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }
}