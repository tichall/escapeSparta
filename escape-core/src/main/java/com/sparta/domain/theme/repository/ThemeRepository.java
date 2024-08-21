package com.sparta.domain.theme.repository;

import com.sparta.domain.theme.entity.Theme;
import com.sparta.global.exception.customException.ThemeException;
import com.sparta.global.exception.errorCode.ThemeErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme, Long>, ThemeRepositoryCustom {
    List<Theme> findAllByStoreId(Long storeId);

    default Theme findByIdOrElseThrow(Long themeId) {
        return findById(themeId).orElseThrow(() ->
                new ThemeException(ThemeErrorCode.THEME_NOT_FOUND));
    }

}
