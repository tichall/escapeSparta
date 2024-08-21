package com.sparta.domain.theme.dto.response;

import com.sparta.domain.theme.entity.Theme;
import com.sparta.domain.theme.entity.ThemeStatus;
import lombok.Getter;

@Getter
public class ThemeDetailResponseDto {
    private Long storeId;
    private Long themeId;
    private String title;
    private String contents;
    private Integer level;
    private Integer duration;
    private Integer minPlayer;
    private Integer maxPlayer;
    private String themeType;
    private Long price;
    private String themeImage;
    private ThemeStatus themeStatus;

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
