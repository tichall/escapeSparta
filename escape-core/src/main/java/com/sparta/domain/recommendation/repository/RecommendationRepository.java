package com.sparta.domain.recommendation.repository;

import com.sparta.domain.recommendation.entity.Recommendation;
import com.sparta.domain.user.entity.User;
import com.sparta.global.exception.customException.RecommendationException;
import com.sparta.global.exception.errorCode.RecommendationErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long>, RecommendationCustom {

    Optional<Recommendation> findByUser(User user);
    Optional<Recommendation> findByIdAndUser(Long recommendationId, User user);

    default void checkFindByUser(User user){
        if(findByUser(user).isPresent()){
            throw new RecommendationException(RecommendationErrorCode.RECOMMENDATION_DUPLICATION);
        }
    }

    default Recommendation findByIdAndUserOrElse(Long recommendationId, User user){
        return findByIdAndUser(recommendationId, user).orElseThrow(() ->
                new RecommendationException(RecommendationErrorCode.RECOMMENDATION_NOT_FOUND));
    }
}
