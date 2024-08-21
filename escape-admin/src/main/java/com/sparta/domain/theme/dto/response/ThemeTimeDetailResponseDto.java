package com.sparta.domain.theme.dto.response;

import com.sparta.domain.theme.entity.ThemeTime;
import com.sparta.global.util.LocalDateTimeUtil;
import lombok.Getter;

@Getter
public class ThemeTimeDetailResponseDto {

  private final Long themeTimeId;
  private final String themeTitle;
  private final String startTime;
  private final String endTime;

  public ThemeTimeDetailResponseDto(ThemeTime themeTime) {
    this.themeTimeId = themeTime.getId();
    this.themeTitle = themeTime.getTheme().getTitle();
    this.startTime = LocalDateTimeUtil.parseLocalDateTimeToString(themeTime.getStartTime());
    this.endTime = LocalDateTimeUtil.parseLocalDateTimeToString(themeTime.getEndTime());
  }
}
