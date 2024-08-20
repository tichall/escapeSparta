package com.sparta.domain.theme.controller;

import com.sparta.domain.theme.dto.request.ThemeTimeCreateRequestDto;
import com.sparta.domain.theme.dto.request.ThemeTimeModifyRequestDto;
import com.sparta.domain.theme.dto.response.ThemeTimeDetailResponseDto;
import com.sparta.domain.theme.service.ThemeTimeAdminService;
import com.sparta.global.response.ResponseMessage;
import com.sparta.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/stores/themes")
@RequiredArgsConstructor
public class ThemeTimeAdminController {

  private final ThemeTimeAdminService themeTimeService;

  /**
   * 테마 예약 시간대 등록
   *
   * @param themeId     테마 id
   * @param requestDto  등록할 시간대 정보
   * @param userDetails 로그인한 관리자 정보
   * @return status.code, message, 등록한 예약 시간대 정보 반환
   */
  @PostMapping("/{themeId}/theme-time")
  public ResponseEntity<ResponseMessage<ThemeTimeDetailResponseDto>> createThemeTime(
      @PathVariable Long themeId,
      @Valid @RequestBody ThemeTimeCreateRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    ThemeTimeDetailResponseDto responseDto = themeTimeService.createThemeTime(themeId, requestDto);

    ResponseMessage<ThemeTimeDetailResponseDto> responseMessage = ResponseMessage.<ThemeTimeDetailResponseDto>builder()
        .statusCode(HttpStatus.CREATED.value())
        .message("방탈출 테마 예약 시간대 등록이 완료되었습니다.")
        .data(responseDto)
        .build();

    return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
  }

  /**
   * 테마 예약 시간대 조회
   *
   * @param themeId     테마 id
   * @param date        날짜
   * @param userDetails 로그인한 관리자 정보
   * @return status.code, message, 예약 시간대 리스트 반환
   */
  @GetMapping("/{themeId}/theme-time")
  public ResponseEntity<ResponseMessage<List<ThemeTimeDetailResponseDto>>> getThemeTimes(
      @PathVariable Long themeId,
      @RequestParam(value = "date", required = false) String date,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<ThemeTimeDetailResponseDto> responseDto = themeTimeService.getThemeTimes(themeId, date);

    ResponseMessage<List<ThemeTimeDetailResponseDto>> responseMessage = ResponseMessage.<List<ThemeTimeDetailResponseDto>>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출 테마 예약 시간대 조회가 완료되었습니다.")
        .data(responseDto)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 테마 예약 시간대 수정
   *
   * @param themeTimeId 예약 시간대 id
   * @param requestDto  수정할 예약 시간대 정보
   * @param userDetails 로그인한 관리자 정보
   * @return status.code, message, 수정된 예약 시간대 정보 반환
   */
  @PutMapping("/theme-time/{themeTimeId}")
  public ResponseEntity<ResponseMessage<ThemeTimeDetailResponseDto>> modifyThemeTime(
      @PathVariable Long themeTimeId,
      @Valid @RequestBody ThemeTimeModifyRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    ThemeTimeDetailResponseDto responseDto = themeTimeService.modifyThemeTime(themeTimeId,
        requestDto);

    ResponseMessage<ThemeTimeDetailResponseDto> responseMessage = ResponseMessage.<ThemeTimeDetailResponseDto>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출 테마 예약 시간대 수정이 완료되었습니다.")
        .data(responseDto)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 테마 예약 시간대 삭제
   *
   * @param themeTimeId 예약 시간대 id
   * @param userDetails 로그인한 관리자 정보
   * @return status.code, message
   */
  @DeleteMapping("/theme-time/{themeTimeId}")
  public ResponseEntity<ResponseMessage<Void>> deleteThemeTime(
      @PathVariable Long themeTimeId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    themeTimeService.deleteThemeTime(themeTimeId);

    ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출 테마 예약 시간대 삭제가 완료되었습니다.")
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

}
