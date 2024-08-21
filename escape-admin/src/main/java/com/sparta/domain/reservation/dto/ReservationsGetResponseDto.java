package com.sparta.domain.reservation.dto;

import com.sparta.domain.reservation.entity.Reservation;
import lombok.Getter;

import java.util.List;

@Getter
public class ReservationsGetResponseDto {

  private final Long themeId;
  private final int totalReservation;
  private final List<ReservationDetailResponseDto> reservationDtoList;

  public ReservationsGetResponseDto(Long themeId, List<Reservation> reservationList) {
    this.themeId = themeId;
    this.totalReservation = reservationList.size();
    this.reservationDtoList = reservationList.stream().map(ReservationDetailResponseDto::new)
        .toList();
  }
}
