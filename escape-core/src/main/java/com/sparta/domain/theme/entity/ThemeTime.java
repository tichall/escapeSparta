package com.sparta.domain.theme.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThemeTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ThemeTimeStatus themeTimeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    @Builder
    public ThemeTime(LocalDateTime startTime, LocalDateTime endTime, Theme theme) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.theme = theme;
        this.themeTimeStatus = ThemeTimeStatus.ENABLE;
    }

    public void updateThemeTime(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void updateThemeTimeStatus() {
        if (this.themeTimeStatus == ThemeTimeStatus.ENABLE) {
            this.themeTimeStatus = ThemeTimeStatus.DISABLE;
        } else {
            this.themeTimeStatus = ThemeTimeStatus.ENABLE;
        }
    }

    public void updateThemeTimeStatus(ThemeTimeStatus themeTimeStatus) {
        this.themeTimeStatus = themeTimeStatus;
    }
}
