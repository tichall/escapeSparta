package com.sparta.domain.review.repository;

import com.sparta.domain.review.entity.Review;
import com.sparta.domain.theme.entity.Theme;
import com.sparta.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepositoryCustom {
    List<Review> findByThemeReview(Theme theme);
    List<Review> findByMyReviews(User user);
    Review findByReview(Long reviewId);
}
