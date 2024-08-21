package com.sparta.domain.recommendation.repository;

import com.sparta.domain.theme.entity.Theme;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationCustom {
    Long findByThemeCount(Theme theme);
}
