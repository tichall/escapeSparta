package com.sparta.domain.theme.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.domain.store.entity.QStore;
import com.sparta.domain.store.entity.StoreStatus;
import com.sparta.domain.theme.entity.QTheme;
import com.sparta.domain.theme.entity.QThemeTime;
import com.sparta.domain.theme.entity.ThemeStatus;
import com.sparta.domain.theme.entity.ThemeTime;
import com.sparta.global.exception.customException.ThemeTimeException;
import com.sparta.global.exception.errorCode.ThemeTimeErrorCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ThemeTimeRepositoryImpl implements ThemeTimeRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  /**
   * 날짜와 테마 ID를 기반으로 테마 시간 목록을 조회합니다.
   *
   * @param themeId 조회할 테마 ID
   * @param date    조회할 날짜
   * @return 조회된 테마 시간 목록
   */
  @Override
  public List<ThemeTime> findThemeTimesByDate(Long themeId, LocalDate date) {
    QThemeTime themeTime = QThemeTime.themeTime;

    LocalDateTime startOfDay = date.atStartOfDay();
    LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

    return jpaQueryFactory.selectFrom(themeTime)
        .where(themeTime.startTime.between(startOfDay, endOfDay)
            .and(themeTime.theme.id.eq(themeId)))
        .fetch();
  }

  /**
   * 활성화된 스토어의 테마 시간 ID를 기반으로 테마 시간 정보를 조회합니다.
   *
   * @param themeTimeId 조회할 테마 시간 ID
   * @return 조회된 테마 시간 정보
   * @throws ThemeTimeException 테마 시간 정보를 찾을 수 없는 경우
   */
  @Override
  public ThemeTime findThemeTimeOfActiveStore(Long themeTimeId) {
    QThemeTime themeTime = QThemeTime.themeTime;
    QTheme theme = QTheme.theme;
    QStore store = QStore.store;

    JPAQuery<ThemeTime> query = jpaQueryFactory.selectFrom(themeTime)
        .leftJoin(themeTime.theme, theme).fetchJoin()
        .leftJoin(theme.store, store).fetchJoin()
        .where(
            themeTime.id.eq(themeTimeId),
            store.storeStatus.eq(StoreStatus.ACTIVE)
        );

    return Optional.ofNullable(query.fetchFirst()).orElseThrow(() ->
        new ThemeTimeException(ThemeTimeErrorCode.THEME_TIME_NOT_FOUND));
  }

  /**
   * 테마 시간 ID를 기반으로 스토어와 테마가 활성화된 상태인지 확인합니다.
   *
   * @param themeTimeId 조회할 테마 시간 ID
   * @return 조회된 테마 시간 정보
   * @throws ThemeTimeException 테마 시간 정보를 찾을 수 없는 경우
   */
  @Override
  public ThemeTime checkStoreAndThemeActive(Long themeTimeId) {
    QThemeTime themeTime = QThemeTime.themeTime;
    QTheme theme = QTheme.theme;
    QStore store = QStore.store;

    JPAQuery<ThemeTime> query = jpaQueryFactory.selectFrom(themeTime)
        .leftJoin(themeTime.theme, theme).fetchJoin()
        .leftJoin(theme.store, store).fetchJoin()
        .where(
            themeTime.id.eq(themeTimeId),
            store.storeStatus.eq(StoreStatus.ACTIVE),
            theme.themeStatus.eq(ThemeStatus.ACTIVE)
        );

    return Optional.ofNullable(query.fetchFirst()).orElseThrow(() ->
        new ThemeTimeException(ThemeTimeErrorCode.THEME_TIME_NOT_FOUND));
  }
}
