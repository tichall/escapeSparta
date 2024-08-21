package com.sparta.domain.theme.repository;

import com.sparta.domain.store.entity.Store;
import com.sparta.domain.theme.entity.Theme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepositoryCustom {
    Page<Theme> findByStore(Store store, Pageable pageable);

    Theme findByActiveTheme(Long themeId);

    Theme findThemeOfActiveStore(Long themeId);
}
