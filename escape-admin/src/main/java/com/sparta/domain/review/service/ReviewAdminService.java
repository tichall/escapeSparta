package com.sparta.domain.review.service;

import com.sparta.domain.review.entity.Review;
import com.sparta.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewAdminService {

  public final ReviewRepository reviewRepository;

  /**
   * 리뷰 강제 삭제
   *
   * @param reviewId 리뷰 id
   */
  @Transactional
  public void deleteReview(Long reviewId) {
    Review review = reviewRepository.findByIdOrElse(reviewId);
    reviewRepository.delete(review);
  }


}
