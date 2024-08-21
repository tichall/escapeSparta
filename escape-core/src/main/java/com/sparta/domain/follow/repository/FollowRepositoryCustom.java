package com.sparta.domain.follow.repository;

import com.sparta.domain.follow.entity.Follow;
import com.sparta.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepositoryCustom {
    List<Follow> findByGetStores(User user);
}
