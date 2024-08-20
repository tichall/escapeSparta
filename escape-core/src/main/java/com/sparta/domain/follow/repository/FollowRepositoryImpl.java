package com.sparta.domain.follow.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.domain.follow.entity.Follow;
import com.sparta.domain.follow.entity.QFollow;
import com.sparta.domain.store.entity.QStore;
import com.sparta.domain.store.entity.StoreStatus;
import com.sparta.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FollowRepositoryImpl implements FollowRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  /**
   * 유저가 팔로우한 스토어 조회합니다.
   *
   * @param user 정보
   * @return user 팔로우한 스토어 목록
   */
  @Override
  public List<Follow> findByGetStores(User user) {
    QFollow follow = QFollow.follow;
    QStore store = QStore.store;

    JPAQuery<Follow> query = jpaQueryFactory.selectFrom(follow)
        .leftJoin(follow.store, store).fetchJoin()
        .where(follow.user.eq(user).and(store.storeStatus.eq(StoreStatus.ACTIVE)));

    return query.fetch();
  }
}
