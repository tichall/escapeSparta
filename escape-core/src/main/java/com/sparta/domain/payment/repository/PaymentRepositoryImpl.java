package com.sparta.domain.payment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.domain.payment.entity.Payment;
import com.sparta.domain.payment.entity.QPayment;
import com.sparta.domain.reservation.entity.QReservation;
import com.sparta.global.exception.customException.PaymentException;
import com.sparta.global.exception.errorCode.PaymentErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PaymentRepositoryImpl implements PaymentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Payment findPaymentByReservationThemeTimeId(Long reservationThemeTimeId) {
        QPayment payment = QPayment.payment;
        QReservation reservation = QReservation.reservation;

        Payment result = queryFactory.selectFrom(payment)
                .join(payment.reservation, reservation).fetchJoin()
                .where(reservation.themeTime.id.eq(reservationThemeTimeId))
                .fetchOne();

        return result;
    }

    @Override
    public Payment findByTid(String tid) {
        QPayment payment = QPayment.payment;
        QReservation reservation = QReservation.reservation;

        Payment result = queryFactory.selectFrom(payment)
                .join(payment.reservation, reservation).fetchJoin()
                .where(payment.tid.eq(tid))
                .fetchOne();

        if (result == null) {
            throw new PaymentException(PaymentErrorCode.PAMENT_NOT_FOUND);
        }
        return result;
    }

    public Payment findByReservationId(Long reservationId) {
        QPayment payment = QPayment.payment;
        QReservation reservation = QReservation.reservation;

        Payment result = queryFactory.selectFrom(payment)
                .join(payment.reservation, reservation).fetchJoin()
                .where(reservation.id.eq(reservationId))
                .fetchOne();

        if (result == null) {
            throw new PaymentException(PaymentErrorCode.PAMENT_NOT_FOUND);
        }

        return result;
    }
}
