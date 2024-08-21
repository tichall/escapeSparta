package com.sparta.domain.reservation.repository;

import com.sparta.domain.reservation.entity.Reservation;
import com.sparta.domain.theme.entity.Theme;
import com.sparta.domain.theme.entity.ThemeTime;
import com.sparta.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepositoryCustom {
   Reservation findByThemeTime(ThemeTime themeTime);
   Reservation findByIdAndActive(Long reservationId, User user);
   List<Reservation> findByUser(User user);
   List<Reservation> findByTheme(Theme theme);
}
