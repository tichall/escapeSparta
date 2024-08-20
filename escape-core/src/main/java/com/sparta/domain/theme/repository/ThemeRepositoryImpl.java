package com.sparta.domain.theme.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.domain.store.entity.QStore;
import com.sparta.domain.store.entity.Store;
import com.sparta.domain.store.entity.StoreStatus;
import com.sparta.domain.theme.entity.QTheme;
import com.sparta.domain.theme.entity.Theme;
import com.sparta.domain.theme.entity.ThemeStatus;
import com.sparta.global.exception.customException.ThemeException;
import com.sparta.global.exception.errorCode.ThemeErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ThemeRepositoryImpl implements ThemeRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  /**
   * 스토어 정보를 기반으로 페이지네이션된 테마 목록을 조회합니다.
   *
   * @param store    조회할 스토어 객체
   * @param pageable 페이지네이션 정보
   * @return 페이지네이션된 테마 목록
   */
  @Override
  public Page<Theme> findByStore(Store store, Pageable pageable) {
    QTheme theme = QTheme.theme;

    JPAQuery<Theme> query = jpaQueryFactory.selectFrom(theme)
        .where(theme.store.eq(store)
            .and(theme.themeStatus.eq(ThemeStatus.ACTIVE)))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    for (Sort.Order order : pageable.getSort()) {
      PathBuilder<Theme> pathBuilder = new PathBuilder<>(theme.getType(), theme.getMetadata());
      query.orderBy(new OrderSpecifier<>(
          order.isAscending() ? Order.ASC : Order.DESC,
          pathBuilder.get(order.getProperty(), Comparable.class)
      ));
    }

    JPAQuery<Long> total = jpaQueryFactory.select(theme.count())
        .from(theme)
        .where(theme.store.eq(store)
            .and(theme.themeStatus.eq(ThemeStatus.ACTIVE)));

    List<Theme> results = query.fetch();

    return PageableExecutionUtils.getPage(results, pageable,
        () -> Optional.ofNullable(total.fetchOne()).orElse(0L));
  }

  /**
   * 활성화된 테마 ID를 기반으로 테마 정보를 조회합니다.
   *
   * @param themeId 조회할 테마 ID
   * @return 조회된 테마 정보
   * @throws ThemeException 테마 정보를 찾을 수 없는 경우
   */
  @Override
  public Theme findByActiveTheme(Long themeId) {
    QTheme theme = QTheme.theme;

    JPAQuery<Theme> query = jpaQueryFactory.selectFrom(theme)
        .where(theme.themeStatus.eq(ThemeStatus.ACTIVE)
            .and(theme.id.eq(themeId)));

    return Optional.ofNullable(query.fetchFirst()).orElseThrow(() ->
        new ThemeException(ThemeErrorCode.THEME_NOT_FOUND));
  }

  /**
   * 활성화된 스토어의 테마 ID를 기반으로 테마 정보를 조회합니다.
   *
   * @param themeId 조회할 테마 ID
   * @return 조회된 테마 정보
   * @throws ThemeException 테마 정보를 찾을 수 없는 경우
   */
  @Override
  public Theme findThemeOfActiveStore(Long themeId) {
    QTheme theme = QTheme.theme;
    QStore store = QStore.store;

    JPAQuery<Theme> query = jpaQueryFactory.selectFrom(theme)
        .leftJoin(theme.store, store).fetchJoin()
        .where(
            theme.id.eq(themeId),
            store.storeStatus.eq(StoreStatus.ACTIVE)
        );

    return Optional.ofNullable(query.fetchFirst()).orElseThrow(() ->
        new ThemeException(ThemeErrorCode.THEME_NOT_FOUND));
  }
}
