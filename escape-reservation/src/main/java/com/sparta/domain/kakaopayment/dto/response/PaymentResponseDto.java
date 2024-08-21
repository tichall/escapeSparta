package com.sparta.domain.kakaopayment.dto.response;

import com.sparta.domain.payment.entity.Payment;
import com.sparta.domain.reservation.entity.PaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PaymentResponseDto {

    private Long id;
    private String cid;
    private String tid;
    private PaymentStatus paymentStatus;
    private Long reservationId;
    private String themeName;
    private LocalDateTime themeTime;

    public PaymentResponseDto(Payment payment) {
        this.id = payment.getId();
        this.cid = payment.getCid();
        this.tid = payment.getTid();
        this.paymentStatus = payment.getPaymentStatus();
        this.reservationId = payment.getReservation().getId();
        this.themeName = payment.getReservation().getTheme().getTitle();
        this.themeTime = payment.getReservation().getThemeTime().getStartTime();
    }
}
