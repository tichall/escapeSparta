package com.sparta.domain.reservation.service;

import com.sparta.domain.reservation.dto.ReservationsGetResponseDto;
import com.sparta.domain.reservation.entity.Reservation;
import com.sparta.domain.reservation.repository.ReservationRepository;
import com.sparta.domain.theme.entity.Theme;
import com.sparta.domain.theme.repository.ThemeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationAdminService {

  private final ReservationRepository reservationRepository;
  private final ThemeRepository themeRepository;

  /**
   * 해당 테마의 예약 내역 조회
   *
   * @param themeId 테마 id
   * @return 예약 내역
   */
  public ReservationsGetResponseDto getReservations(Long themeId) {
    Theme theme = themeRepository.findThemeOfActiveStore(themeId);

    List<Reservation> reservationList = reservationRepository.findByTheme(theme);
    return new ReservationsGetResponseDto(themeId, reservationList);
  }

  /**
   * 예약 강제 취소
   *
   * @param reservationId 예약 id
   */
  @Transactional
  public void cancelReservation(Long reservationId) {
    Reservation reservation = reservationRepository.findActiveReservation(reservationId);
    reservation.cancelReservation();
  }
}