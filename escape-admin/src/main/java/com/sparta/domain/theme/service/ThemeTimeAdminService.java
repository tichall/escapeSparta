package com.sparta.domain.theme.service;

import com.sparta.domain.theme.dto.request.ThemeTimeCreateRequestDto;
import com.sparta.domain.theme.dto.request.ThemeTimeModifyRequestDto;
import com.sparta.domain.theme.dto.response.ThemeTimeDetailResponseDto;
import com.sparta.domain.theme.entity.Theme;
import com.sparta.domain.theme.entity.ThemeTime;
import com.sparta.domain.theme.repository.ThemeRepository;
import com.sparta.domain.theme.repository.ThemeTimeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.sparta.global.util.LocalDateTimeUtil.*;

@Service
@RequiredArgsConstructor
public class ThemeTimeAdminService {

  private final ThemeTimeRepository themeTimeRepository;
  private final ThemeRepository themeRepository;

  /**
   * 테마 예약 시간대 등록
   *
   * @param themeId    테마 id
   * @param requestDto 등록할 시간대 정보
   * @return 등록한 예약 시간대 정보
   */
  @Transactional
  public ThemeTimeDetailResponseDto createThemeTime(Long themeId,
      ThemeTimeCreateRequestDto requestDto) {
    Theme theme = themeRepository.findThemeOfActiveStore(themeId);

    LocalDateTime startTime = parseDateTimeStringToLocalDateTime(requestDto.getStartTime());
    LocalDateTime endTime = calculateEndTime(startTime, theme.getDuration());

    ThemeTime themeTime = ThemeTime.builder()
        .startTime(startTime)
        .endTime(endTime)
        .theme(theme)
        .build();

    themeTimeRepository.save(themeTime);
    return new ThemeTimeDetailResponseDto(themeTime);
  }

  /**
   * 테마 예약 시간대 조회
   *
   * @param themeId 테마 id
   * @param date    날짜
   * @return 예약 시간대 리스트
   */
  public List<ThemeTimeDetailResponseDto> getThemeTimes(Long themeId, String date) {
    Theme theme = themeRepository.findThemeOfActiveStore(themeId);
    List<ThemeTime> themeTimes;

    if (date == null) {
      themeTimes = themeTimeRepository.findAllByThemeId(theme.getId());
    } else {
      LocalDate searchDate = parseDateStringToLocalDate(date);
      themeTimes = themeTimeRepository.findThemeTimesByDate(themeId, searchDate);
    }

    return themeTimes.stream().map(ThemeTimeDetailResponseDto::new).toList();
  }

  /**
   * 테마 예약 시간대 수정
   *
   * @param themeTimeId 예약 시간대 id
   * @param requestDto  수정할 예약 시간대 정보
   * @return 수정된 예약 시간대 정보
   */
  @Transactional
  public ThemeTimeDetailResponseDto modifyThemeTime(Long themeTimeId,
      ThemeTimeModifyRequestDto requestDto) {
    ThemeTime themeTime = themeTimeRepository.findThemeTimeOfActiveStore(themeTimeId);

    LocalDateTime startTime = parseDateTimeStringToLocalDateTime(requestDto.getStartTime());
    LocalDateTime endTime = calculateEndTime(startTime, themeTime.getTheme().getDuration());

    themeTime.updateThemeTime(startTime, endTime);
    themeTimeRepository.save(themeTime);

    return new ThemeTimeDetailResponseDto(themeTime);
  }

  /**
   * 테마 예약 시간대 삭제
   *
   * @param themeTimeId 예약 시간대 id
   */
  @Transactional
  public void deleteThemeTime(Long themeTimeId) {
    ThemeTime themeTime = themeTimeRepository.findThemeTimeOfActiveStore(themeTimeId);
    themeTimeRepository.delete(themeTime);
  }

}