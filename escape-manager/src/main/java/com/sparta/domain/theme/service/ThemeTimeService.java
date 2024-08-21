package com.sparta.domain.theme.service;

import com.sparta.domain.theme.dto.request.ThemeTimeCreateRequestDto;
import com.sparta.domain.theme.dto.request.ThemeTimeModifyRequestDto;
import com.sparta.domain.theme.dto.response.ThemeTimeDetailResponseDto;
import com.sparta.domain.theme.entity.Theme;
import com.sparta.domain.theme.entity.ThemeTime;
import com.sparta.domain.theme.repository.ThemeRepository;
import com.sparta.domain.theme.repository.ThemeTimeRepository;
import com.sparta.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.sparta.global.util.LocalDateTimeUtil.*;

@Service
@RequiredArgsConstructor
public class ThemeTimeService {

  private final ThemeTimeRepository themeTimeRepository;
  private final ThemeRepository themeRepository;

  /**
   * 테마 예약 시간대 등록
   *
   * @param themeId    테마 id
   * @param requestDto 등록할 시간대 정보
   * @param user       로그인한 매니저
   * @return 등록한 예약 시간대 정보
   */
  @Transactional
  public ThemeTimeDetailResponseDto createThemeTime(Long themeId,
      ThemeTimeCreateRequestDto requestDto, User user) {
    Theme theme = themeRepository.findThemeOfActiveStore(themeId);
    theme.getStore().checkManager(user);

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
   * @param user    로그인한 매니저
   * @return 예약 시간대 리스트
   */
  public List<ThemeTimeDetailResponseDto> getThemeTimes(Long themeId, String date, User user) {
    Theme theme = themeRepository.findThemeOfActiveStore(themeId);
    theme.getStore().checkManager(user);

    List<ThemeTime> themeTimes;
    if (date == null) {
      // 매니저는 비활성화된 Theme의 예약 시간대도 조회 가능
      themeTimes = themeTimeRepository.findAllByThemeId(themeId);
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
   * @param user        로그인한 매니저
   * @return 수정된 예약 시간대 정보
   */
  @Transactional
  public ThemeTimeDetailResponseDto modifyThemeTime(Long themeTimeId,
      ThemeTimeModifyRequestDto requestDto, User user) {
    ThemeTime themeTime = themeTimeRepository.findThemeTimeOfActiveStore(themeTimeId);
    themeTime.getTheme().getStore().checkManager(user);

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
   * @param user        로그인한 매니저
   */
  @Transactional
  public void deleteThemeTime(Long themeTimeId, User user) {
    ThemeTime themeTime = themeTimeRepository.findThemeTimeOfActiveStore(themeTimeId);
    themeTime.getTheme().getStore().checkManager(user);

    themeTimeRepository.delete(themeTime);
  }

}