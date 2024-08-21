package com.sparta.domain.reservation.dto;

import com.sparta.domain.reservation.entity.PaymentStatus;
import com.sparta.domain.reservation.entity.Reservation;
import com.sparta.domain.reservation.entity.ReservationStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReservationDetailResponseDto {

  private Long reservationId;
  private String storeName;
  private String themeImage;
  private String themeTitle;
  private LocalDateTime startTime;
  private Integer player;
  private Long price;
  private PaymentStatus paymentStatus;
  private ReservationStatus reservationStatus;
  private LocalDateTime createdAt;

  public ReservationDetailResponseDto(Reservation reservation) {
    this.reservationId = reservation.getId();
    this.themeImage = reservation.getTheme().getThemeImage();
    this.themeTitle = reservation.getTheme().getTitle();
    this.storeName = reservation.getTheme().getStore().getName();
    this.startTime = reservation.getThemeTime().getStartTime();
    this.player = reservation.getPlayer();
    this.price = reservation.getPrice();
//        this.paymentStatus = reservation.getPaymentStatus();
    this.reservationStatus = reservation.getReservationStatus();
    this.createdAt = reservation.getCreatedAt();
  }
}
