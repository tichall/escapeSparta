package com.sparta.domain.review.repository;

import com.sparta.domain.reservation.entity.Reservation;
import com.sparta.domain.review.entity.Review;
import com.sparta.domain.user.entity.User;
import com.sparta.global.exception.customException.ReviewException;
import com.sparta.global.exception.errorCode.ReviewErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long>, ReviewRepositoryCustom{

    Optional<Review> findByIdAndUser(Long reviewId, User user);
    Optional<Review> findByUserAndReservation(User user, Reservation reservation);

    default Review findByIdOrElse(Long id){
        return findById(id).orElseThrow(
                () -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));
    }

    default Review findByIdAndUserOrElse(Long reviewId, User user){
        return findByIdAndUser(reviewId, user).orElseThrow(
                () -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));
    }

    default void checkIfAlreadyReview(User user, Reservation reservation){
        if (findByUserAndReservation(user, reservation).isPresent()) {
            throw new ReviewException(ReviewErrorCode.REVIEW_DUPLICATION);
        }
    }
}
