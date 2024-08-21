package com.sparta.domain.theme.repository;

import com.sparta.domain.theme.entity.Theme;
import com.sparta.domain.theme.entity.ThemeTime;
import com.sparta.global.exception.customException.ThemeTimeException;
import com.sparta.global.exception.errorCode.ThemeTimeErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThemeTimeRepository extends JpaRepository<ThemeTime, Long>, ThemeTimeRepositoryCustom {
    List<ThemeTime> findByTheme(Theme theme);

    List<ThemeTime> findAllByThemeId(Long themeId);

    default ThemeTime findByIdOrElse(Long themeId){
        return findById(themeId).orElseThrow(() ->
                new ThemeTimeException(ThemeTimeErrorCode.THEME_TIME_NOT_FOUND));
    }
}
