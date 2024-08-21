package com.sparta.domain.payment.repository;

import com.sparta.domain.payment.entity.Payment;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepositoryCustom {
    Payment findPaymentByReservationThemeTimeId(Long reservationThemeTimeId);

    Payment findByTid(String tid);

    Payment findByReservationId(Long reservationId);
}
