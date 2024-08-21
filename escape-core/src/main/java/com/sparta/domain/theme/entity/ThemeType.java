package com.sparta.domain.theme.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ThemeType {
    MYSTERY("미스터리"),
    HORROR("호러"),
    FANTASY("판타지"),
    ADVENTURE("어드벤처"),
    COMEDY("코미디"),
    SF("SF"),
    THRILLER("스릴러");

    private final String name;
}
