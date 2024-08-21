package com.sparta.domain.theme.dto;

import com.sparta.domain.theme.entity.Theme;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ThemeInfoResponseDto {
    private Long themeId;
    private String title;
    private String description;
    private Integer level;
    private Integer minPlayer;
    private Integer maxPlayer;
    private Integer duration;
    private String themeImage;
    private Long price;
    private Long recommendationCount;

    public ThemeInfoResponseDto(Theme theme, Long recommendationCount){
        this.themeId = theme.getId();
        this.title = theme.getTitle();
        this.description = theme.getContents();
        this.level = theme.getLevel();
        this.minPlayer = theme.getMinPlayer();;
        this.maxPlayer = theme.getMaxPlayer();
        this.duration = theme.getDuration();
        this.themeImage = theme.getThemeImage();
        this.price = theme.getPrice();
        this.recommendationCount = recommendationCount;
    }
}
