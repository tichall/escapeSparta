package com.sparta.domain.theme.repository;

import com.sparta.domain.theme.entity.ThemeTime;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ThemeTimeRepositoryCustom {

    List<ThemeTime> findThemeTimesByDate(Long themeId, LocalDate date);

    ThemeTime findThemeTimeOfActiveStore(Long themeTimeId);

    ThemeTime checkStoreAndThemeActive(Long themeTimeId);
}
