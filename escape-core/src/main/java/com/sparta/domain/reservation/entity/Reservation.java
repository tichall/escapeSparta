package com.sparta.domain.reservation.entity;

import com.sparta.domain.theme.entity.Theme;
import com.sparta.domain.theme.entity.ThemeTime;
import com.sparta.domain.user.entity.User;
import com.sparta.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // payment 추가
  @Column(nullable = false)
  private Integer player; //플레이 인원

  @Column(nullable = false)
  private Long price;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReservationStatus reservationStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "theme_id", nullable = false)
  private Theme theme;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "theme_time_id", nullable = false)
  private ThemeTime themeTime;

  @Builder
  public Reservation(Integer player, Long price,
      ReservationStatus reservationStatus, User user, Theme theme, ThemeTime themeTime) {
    this.player = player;
    this.price = price;
    this.reservationStatus = reservationStatus;
    this.user = user;
    this.theme = theme;
    this.themeTime = themeTime;
  }

  public void updateReservationStatus() {
    this.reservationStatus = ReservationStatus.COMPLETE;
  }

  public void cancelReservation() {
    this.reservationStatus = ReservationStatus.CANCEL;
    this.themeTime.updateThemeTimeStatus();
  }

}
