package com.sparta.dto;

import com.sparta.domain.reservation.entity.PaymentStatus;
import com.sparta.domain.reservation.entity.Reservation;
import com.sparta.service.ReservationService;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreateReservationResponseDto {
    private Long reservationId;
    private Integer player;
    private Long price;
    private PaymentStatus paymentStatus;
    private LocalDateTime createAt;

    public CreateReservationResponseDto(Reservation reservation){
        reservationId = reservation.getId();
        player = reservation.getPlayer();
        price = reservation.getPrice();
        paymentStatus = reservation.getPaymentStatus();
        createAt = reservation.getCreatedAt();
    }
}
