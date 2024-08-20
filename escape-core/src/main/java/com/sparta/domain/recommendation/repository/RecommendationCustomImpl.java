package com.sparta.domain.recommendation.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.domain.recommendation.entity.QRecommendation;
import com.sparta.domain.theme.entity.Theme;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RecommendationCustomImpl implements RecommendationCustom {

  private final JPAQueryFactory jpaQueryFactory;

  /**
   * 테마 추천 수를 조회합니다.
   *
   * @param theme 조회할 테마 객체
   * @return 조회된 추천 수
   */
  @Override
  public Long findByThemeCount(Theme theme) {

    QRecommendation recommendation = QRecommendation.recommendation;

    JPAQuery<Long> query = jpaQueryFactory.select(recommendation.count())
        .from(recommendation)
        .where(recommendation.theme.eq(theme));

    return Optional.ofNullable(query.fetchOne()).orElse(0L);
  }
}
