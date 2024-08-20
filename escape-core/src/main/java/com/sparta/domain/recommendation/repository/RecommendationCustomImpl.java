package com.sparta.domain.recommendation.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.domain.recommendation.entity.QRecommendation;
import com.sparta.domain.theme.entity.Theme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RecommendationCustomImpl implements RecommendationCustom {
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 테마의 추천수 조회
     *
     * @param theme 테마
     * @return 추천수
     */
    @Override
    public Long findByThemeCount(Theme theme) {

        QRecommendation recommendation = QRecommendation.recommendation;

        JPAQuery<Long> query = jpaQueryFactory.select(recommendation.count()).from(recommendation).where(recommendation.theme.eq(theme));

        return Optional.ofNullable(query.fetchOne()).orElse(0L);
    }
}
