package com.sparta.domain.reservation.controller;

import com.sparta.domain.reservation.dto.ReservationsGetResponseDto;
import com.sparta.domain.reservation.service.ReservationAdminService;
import com.sparta.global.response.ResponseMessage;
import com.sparta.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/stores/themes")
@RequiredArgsConstructor
public class ReservationAdminController {

  private final ReservationAdminService reservationService;

  /**
   * 해당 테마의 예약 내역 조회
   *
   * @param themeId     테마 id
   * @param userDetails 로그인한 관리자 정보
   * @return status.code, message, 예약 내역 반환
   */
  @GetMapping("/{themeId}/reservations")
  public ResponseEntity<ResponseMessage<ReservationsGetResponseDto>> getReservations(
      @PathVariable Long themeId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    ReservationsGetResponseDto responseDto = reservationService.getReservations(themeId);

    ResponseMessage<ReservationsGetResponseDto> responseMessage = ResponseMessage.<ReservationsGetResponseDto>builder()
        .statusCode(HttpStatus.OK.value())
        .message("해당 방탈출 테마의 예약 내역 조회가 완료되었습니다.")
        .data(responseDto)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 예약 강제 취소
   *
   * @param reservationId 예약 Id
   * @param userDetails   로그인한 관리자 정보
   * @return status.code, message 반환
   */
  @DeleteMapping("/reservations/{reservationId}")
  public ResponseEntity<ResponseMessage<Void>> cancelReservation(
      @PathVariable Long reservationId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    reservationService.cancelReservation(reservationId);

    ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
        .statusCode(HttpStatus.OK.value())
        .message("해당 예약 취소가 완료되었습니다.")
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }
}