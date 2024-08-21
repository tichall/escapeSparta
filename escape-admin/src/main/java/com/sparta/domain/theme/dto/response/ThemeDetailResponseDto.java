package com.sparta.domain.theme.dto.response;

import com.sparta.domain.theme.entity.Theme;
import com.sparta.domain.theme.entity.ThemeStatus;
import lombok.Getter;

@Getter
public class ThemeDetailResponseDto {
    private final Long storeId;
    private final Long themeId;
    private final String title;
    private final String contents;
    private final Integer level;
    private final Integer duration;
    private final Integer minPlayer;
    private final Integer maxPlayer;
    private final String themeType;
    private final Long price;
    private final String themeImage;
    private final ThemeStatus themeStatus;

    public ThemeDetailResponseDto(Theme theme) {
        this.storeId = theme.getStore().getId();
        this.themeId = theme.getId();
        this.title = theme.getTitle();
        this.contents = theme.getContents();
        this.level = theme.getLevel();
        this.duration = theme.getDuration();
        this.minPlayer = theme.getMinPlayer();
        this.maxPlayer = theme.getMaxPlayer();
        this.themeType = theme.getThemeType().getName();
        this.price = theme.getPrice();
        this.themeImage = theme.getThemeImage();
        this.themeStatus = theme.getThemeStatus();
    }
}
