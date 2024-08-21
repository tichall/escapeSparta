package com.sparta.domain.theme.entity;

import com.sparta.domain.reservation.entity.Reservation;
import com.sparta.domain.store.entity.Store;
import com.sparta.global.entity.TimeStamped;
import com.sparta.global.exception.customException.ThemeException;
import com.sparta.global.exception.errorCode.ThemeErrorCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Theme extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    @Min(1) @Max(5)
    private Integer level = 0;

    @Column(nullable = false)
    @Min(1)
    private Integer duration;

    @Column(nullable = false)
    @Min(1)
    private Integer minPlayer; // 플레이 인원

    @Column(nullable = false)
    @Max(10)
    private Integer maxPlayer; // 플레이 인원

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ThemeType themeType;

    private String themeImage;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ThemeStatus themeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "theme", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    @Builder
    public Theme(String title, String contents, Integer level, Integer duration, Integer minPlayer, Integer maxPlayer, ThemeType themeType, String themeImage, Long price, ThemeStatus themeStatus, Store store) {
        this.title = title;
        this.contents = contents;
        this.level = level;
        this.duration = duration;
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
        this.themeType = themeType;
        this.themeImage = themeImage;
        this.price = price;
        this.themeStatus = themeStatus;
        this.store = store;
    }

    public void updateTheme(String title, String contents, Integer level, Integer duration, Integer minPlayer, Integer maxPlayer,ThemeType themeType, Long price) {
        this.title = title;
        this.contents = contents;
        this.level = level;
        this.duration = duration;
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
        this.themeType = themeType;
        this.price = price;
    }

    public void updateThemeImage(String themeImage) {
        this.themeImage = themeImage;
    }

    public void deleteThemeImage() {
        this.themeImage = null;
    }

    public void toggleThemeStatus() {
        this.themeStatus = this.themeStatus == ThemeStatus.ACTIVE ? ThemeStatus.INACTIVE : ThemeStatus.ACTIVE;

    }

    public void verifyThemeIsActive() {
        if(!this.themeStatus.equals(ThemeStatus.ACTIVE)) {
            throw new ThemeException(ThemeErrorCode.INVALID_THEME_STATUS);
        }
    }
}
