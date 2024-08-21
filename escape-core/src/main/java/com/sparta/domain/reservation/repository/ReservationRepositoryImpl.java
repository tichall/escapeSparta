package com.sparta.domain.reservation.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.domain.reservation.entity.QReservation;
import com.sparta.domain.reservation.entity.Reservation;
import com.sparta.domain.reservation.entity.ReservationStatus;
import com.sparta.domain.store.entity.QStore;
import com.sparta.domain.theme.entity.QTheme;
import com.sparta.domain.theme.entity.Theme;
import com.sparta.domain.theme.entity.ThemeTime;
import com.sparta.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;


  /**
   * 테마 시간을 기반으로 예약 정보를 조회합니다.
   *
   * @param themeTime 조회할 테마 시간 객체
   * @return 조회된 예약 정보
   */
  @Override
  public Reservation findByThemeTime(ThemeTime themeTime) {
    QReservation reservation = QReservation.reservation;

    JPAQuery<Reservation> query = jpaQueryFactory.selectFrom(reservation)
        .where(reservation.themeTime.eq(themeTime),
            reservation.reservationStatus.eq(ReservationStatus.COMPLETE));

    return query.fetchFirst();
  }

  /**
   * 예약 ID와 user 기반으로 활성화된 예약 정보를 조회합니다.
   *
   * @param reservationId 조회할 예약 ID
   * @param user
   * @return 조회된 예약 정보
   */
  @Override
  public Reservation findByIdAndActive(Long reservationId, User user) {
    QReservation reservation = QReservation.reservation;

    JPAQuery<Reservation> query = jpaQueryFactory.selectFrom(reservation)
        .where(reservation.id.eq(reservationId),
            reservation.reservationStatus.eq(ReservationStatus.COMPLETE),
            reservation.user.eq(user));

    return query.fetchFirst();
  }

  /**
   * 사용자 정보를 기반으로 예약 목록을 조회합니다.
   *
   * @param user 객체
   * @return 조회된 예약 목록
   */
  @Override
  public List<Reservation> findByUser(User user) {
    QReservation reservation = QReservation.reservation;
    QTheme theme = QTheme.theme;
    QStore store = QStore.store;

    JPAQuery<Reservation> query = jpaQueryFactory.selectFrom(reservation)
        .leftJoin(reservation.theme, theme).fetchJoin()
        .leftJoin(theme.store, store).fetchJoin()
        .where(reservation.user.eq(user)
            .and(reservation.reservationStatus.eq(ReservationStatus.COMPLETE)
                .or(reservation.reservationStatus.eq(ReservationStatus.CANCEL))))
        .orderBy(reservation.createdAt.desc());

    return query.fetch();
  }

  /**
   * 테마 정보를 기반으로 예약 목록을 조회합니다.
   *
   * @param theme 조회할 테마 객체
   * @return 조회된 예약 목록
   */
  @Override
  public List<Reservation> findByTheme(Theme theme) {
    QReservation reservation = QReservation.reservation;
    QTheme qTheme = QTheme.theme;
    QStore store = QStore.store;

    return jpaQueryFactory.selectFrom(reservation)
        .leftJoin(reservation.theme, qTheme).fetchJoin()
        .leftJoin(reservation.theme.store, store).fetchJoin()
        .where(reservation.theme.eq(theme))
        .fetch();
  }
}
