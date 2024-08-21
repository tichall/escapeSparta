package com.sparta.domain.user.repository;

import com.sparta.domain.user.entity.User;
import com.sparta.domain.user.entity.UserType;
import com.sparta.global.exception.customException.UserException;
import com.sparta.global.exception.errorCode.UserErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByUserType(UserType userType);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    default User findByIdOrElseThrow(Long userId) {
        return findById(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    default User findByEmailOrElseThrow(String email) {
        return findByEmail(email).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    default void throwIfEmailExists(String email) {
        if (existsByEmail(email)) {
            throw new UserException(UserErrorCode.USER_DUPLICATION);
        }
    }
}
