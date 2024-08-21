package com.sparta.domain.reservation.service;

import com.sparta.domain.reservation.dto.ReservationListResponseDto;
import com.sparta.domain.reservation.entity.Reservation;
import com.sparta.domain.reservation.repository.ReservationRepository;
import com.sparta.domain.theme.entity.Theme;
import com.sparta.domain.theme.repository.ThemeRepository;
import com.sparta.domain.user.entity.User;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final ThemeRepository themeRepository;

  /**
   * 해당 테마의 예약 내역 조회
   *
   * @param themeId 테마 id
   * @param user    로그인한 매니저
   * @return 예약 내역
   */
  public ReservationListResponseDto getReservations(Long themeId, User user) {
    Theme theme = themeRepository.findThemeOfActiveStore(themeId);
    theme.getStore().checkManager(user);

    List<Reservation> reservationList = reservationRepository.findByTheme(theme);
    return new ReservationListResponseDto(themeId, reservationList);
  }

  /**
   * 예약 강제 취소
   *
   * @param reservationId 예약 id
   * @param user          로그인한 매니저
   */
  @Transactional
  public void cancelReservation(Long reservationId, User user) {
    Reservation reservation = reservationRepository.findActiveReservation(reservationId);
    reservation.getTheme().getStore().checkManager(user);
    reservation.cancelReservation();
  }
}