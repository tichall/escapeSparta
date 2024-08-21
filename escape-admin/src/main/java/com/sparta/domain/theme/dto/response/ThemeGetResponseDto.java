package com.sparta.domain.theme.dto.response;

import com.sparta.domain.theme.entity.Theme;
import lombok.Getter;

import java.util.List;

@Getter
public class ThemeGetResponseDto {
    private final int totalTheme;
    private final List<ThemeDetailResponseDto> themeDtoList;

    public ThemeGetResponseDto(List<Theme> themeList
    ) {
        this.totalTheme = themeList.size();
        this.themeDtoList = themeList.stream().map(ThemeDetailResponseDto::new).toList();
    }
}
