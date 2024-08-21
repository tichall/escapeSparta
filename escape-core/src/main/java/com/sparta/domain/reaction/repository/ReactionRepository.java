package com.sparta.domain.reaction.repository;

import com.sparta.domain.reaction.entity.Reaction;
import com.sparta.domain.review.entity.Review;
import com.sparta.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction,Long> {

    Optional<Reaction> findByReviewAndUser(Review review, User user);

//    default Reaction findByIdOrElseThrow(Long reactionId){
//        return findById(reactionId).orElseThrow(() ->
//                new ReactionException()
//    }
}
