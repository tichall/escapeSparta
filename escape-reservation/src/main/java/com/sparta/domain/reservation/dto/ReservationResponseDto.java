package com.sparta.domain.reservation.dto;

import com.sparta.domain.reservation.entity.Reservation;
import com.sparta.domain.reservation.entity.ReservationStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationResponseDto {

  private Long reservationId;
  private String themeImage;
  private String themeTitle;
  private LocalDateTime createAt;
  private LocalDateTime startTime;
  private String storeName;
  private Integer player;
  private Long price;
  private ReservationStatus reservationStatus;

  public ReservationResponseDto(Reservation reservation) {
    this.reservationId = reservation.getId();
    this.themeImage = reservation.getTheme().getThemeImage();
    this.themeTitle = reservation.getTheme().getTitle();
    this.createAt = reservation.getCreatedAt();
    this.startTime = reservation.getThemeTime().getStartTime();
    this.storeName = reservation.getTheme().getStore().getName();
    this.player = reservation.getPlayer();
    this.price = reservation.getPrice();
    this.reservationStatus = reservation.getReservationStatus();
  }
}
