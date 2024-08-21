package com.sparta.domain.follow.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.domain.follow.entity.Follow;
import com.sparta.domain.follow.entity.QFollow;
import com.sparta.domain.store.entity.QStore;
import com.sparta.domain.store.entity.StoreStatus;
import com.sparta.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FollowRepositoryImpl implements FollowRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

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
