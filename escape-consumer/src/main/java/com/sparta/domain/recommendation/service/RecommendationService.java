package com.sparta.domain.recommendation.service;

import com.sparta.domain.recommendation.entity.Recommendation;
import com.sparta.domain.recommendation.repository.RecommendationRepository;
import com.sparta.domain.theme.entity.Theme;
import com.sparta.domain.theme.repository.ThemeRepository;
import com.sparta.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final ThemeRepository themeRepository;

    /**
     * 좋아요 등록
     *
     * @param user    로그인 유저
     * @param themeId 좋아요할 테마 id
     */
    @Transactional
    public void createRecommendation(User user, Long themeId) {
        Theme theme = themeRepository.findByIdOrElseThrow(themeId);
        recommendationRepository.checkFindByUser(user);

        Recommendation recommendation = Recommendation.builder()
                .user(user)
                .theme(theme)
                .build();

        recommendationRepository.save(recommendation);
    }

    /**
     * 좋아요 취소
     *
     * @param user             로그인 유저
     * @param recommendationId 좋아요 id
     */
    @Transactional
    public void deleteRecommendation(User user, Long recommendationId) {
        Recommendation recommendation = recommendationRepository.findByIdAndUserOrElse(recommendationId, user);
        recommendationRepository.delete(recommendation);
    }
}
