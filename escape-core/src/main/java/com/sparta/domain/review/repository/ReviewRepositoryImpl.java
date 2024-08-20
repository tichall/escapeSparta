package com.sparta.domain.review.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.domain.reservation.entity.QReservation;
import com.sparta.domain.review.entity.QReview;
import com.sparta.domain.review.entity.Review;
import com.sparta.domain.store.entity.QStore;
import com.sparta.domain.theme.entity.QTheme;
import com.sparta.domain.theme.entity.Theme;
import com.sparta.domain.user.entity.QUser;
import com.sparta.domain.user.entity.User;
import com.sparta.global.exception.customException.ReviewException;
import com.sparta.global.exception.errorCode.ReviewErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 해당 테마의 모든 리뷰조회
     *
     * @param theme 테마
     * @return List<Review> 리뷰리스트
     */
    @Override
    public List<Review> findByThemeReview(Theme theme) {

        QReview review = QReview.review;
        QUser user = QUser.user;
        QReservation reservation = QReservation.reservation;
        QTheme qTheme = QTheme.theme;

        JPAQuery<Review> query = jpaQueryFactory.selectFrom(review)
                .leftJoin(review.user, user).fetchJoin()
                .leftJoin(review.reservation, reservation).fetchJoin()
                .leftJoin(reservation.theme, qTheme).fetchJoin()
                .where(qTheme.eq(theme));

        return query.fetch();
    }

    /**
     * 내가 쓴 모든 리뷰
     *
     * @param user 로그인한 유저
     * @return List<Review> 리뷰리스트
     */
    @Override
    public List<Review> findByMyReviews(User user) {
        QReview review = QReview.review;
        QTheme theme = QTheme.theme;
        QStore store = QStore.store;
        QUser qUser = QUser.user;

        JPAQuery<Review> query = jpaQueryFactory.selectFrom(review)
                .leftJoin(review.user, qUser).fetchJoin()
                .leftJoin(review.theme, theme).fetchJoin()
                .leftJoin(theme.store, store).fetchJoin()
                .where(review.user.eq(user));

        return query.fetch();
    }

    /**
     * 리뷰 id로 리뷰조회
     *
     * @param reviewId 리뷰 id
     * @return Review 리뷰
     */
    @Override
    public Review findByReview(Long reviewId) {
        QReview review = QReview.review;
        QTheme theme = QTheme.theme;
        QStore store = QStore.store;
        QUser qUser = QUser.user;

        JPAQuery<Review> query = jpaQueryFactory.selectFrom(review)
                .leftJoin(review.user, qUser).fetchJoin()
                .leftJoin(review.theme, theme).fetchJoin()
                .leftJoin(theme.store, store).fetchJoin()
                .where(review.id.eq(reviewId));

        return Optional.ofNullable(query.fetchFirst())
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));
    }

}
