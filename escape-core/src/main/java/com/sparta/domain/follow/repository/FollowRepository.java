package com.sparta.domain.follow.repository;

import com.sparta.domain.follow.entity.Follow;
import com.sparta.domain.store.entity.Store;
import com.sparta.domain.user.entity.User;
import com.sparta.global.exception.customException.FollowException;
import com.sparta.global.exception.errorCode.FollowErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom {

    Optional<Follow> findByUserAndStore(User user, Store store);

    default void checkIfAlreadyFollowed(User user, Store store){
        if (findByUserAndStore(user, store).isPresent()) {
            throw new FollowException(FollowErrorCode.FOLLOW_DUPLICATION);
        }
    }

    default Follow getFollowOrThrow(User user, Store store){
        return findByUserAndStore(user, store).orElseThrow(() ->
                new FollowException(FollowErrorCode.FOLLOW_NOT_STORE));
    }
}
