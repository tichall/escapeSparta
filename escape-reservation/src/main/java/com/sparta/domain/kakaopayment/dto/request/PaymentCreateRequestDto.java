package com.sparta.domain.kakaopayment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreateRequestDto {
    private Long reservationId;
    private String tid;
}
