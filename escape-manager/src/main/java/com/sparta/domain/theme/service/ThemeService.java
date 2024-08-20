package com.sparta.domain.theme.service;

import com.sparta.domain.s3.S3Uploader;
import com.sparta.domain.store.entity.Store;
import com.sparta.domain.store.repository.StoreRepository;
import com.sparta.domain.theme.dto.request.ThemeCreateRequestDto;
import com.sparta.domain.theme.dto.request.ThemeModifyRequestDto;
import com.sparta.domain.theme.dto.response.ThemeDetailResponseDto;
import com.sparta.domain.theme.dto.response.ThemeGetResponseDto;
import com.sparta.domain.theme.entity.Theme;
import com.sparta.domain.theme.entity.ThemeStatus;
import com.sparta.domain.theme.repository.ThemeRepository;
import com.sparta.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService {

  private final ThemeRepository themeRepository;
  private final StoreRepository storeRepository;
  private final S3Uploader s3Uploader;

  /**
   * 방탈출 테마 등록
   *
   * @param file       테마 이미지 파일
   * @param requestDto 테마 정보 Dto
   * @param user       로그인한 매니저
   * @return 등록한 테마 정보
   */
  @Transactional
  public ThemeDetailResponseDto createTheme(MultipartFile file, ThemeCreateRequestDto requestDto,
      User user) {
    Store store = storeRepository.findByActiveStore(requestDto.getStoreId());
    store.checkManager(user);

    Theme theme = Theme.builder()
        .title(requestDto.getTitle())
        .contents(requestDto.getContents())
        .level(requestDto.getLevel())
        .duration(requestDto.getDuration())
        .minPlayer(requestDto.getMinPlayer())
        .maxPlayer(requestDto.getMaxPlayer())
        .price(requestDto.getPrice())
        .themeType(requestDto.getThemeType())
        .themeStatus(ThemeStatus.ACTIVE)
        .store(store)
        .build();

    themeRepository.save(theme);

    String themeImage = s3Uploader.uploadThemeImage(file, store.getId(), theme.getId());
    theme.updateThemeImage(themeImage);

    return new ThemeDetailResponseDto(theme);
  }

  /**
   * 방탈출 카페의 전체 테마 조회
   *
   * @param storeId 카페 id
   * @param user    로그인한 매니저 정보
   * @return 전체 테마 리스트
   */
  public ThemeGetResponseDto getThemes(Long storeId, User user) {
    Store findStore = storeRepository.findByActiveStore(storeId);
    findStore.checkManager(user);

    List<Theme> themeList = themeRepository.findAllByStoreId(storeId);
    return new ThemeGetResponseDto(themeList);
  }

  /**
   * 방탈출 테마 수정
   *
   * @param themeId    테마 id
   * @param requestDto 수정할 테마 정보 Dto
   * @param user       로그인한 매니저
   * @return 수정한 테마 정보
   */
  @Transactional
  public ThemeDetailResponseDto modifyTheme(Long themeId, ThemeModifyRequestDto requestDto,
      User user) {
    Theme theme = themeRepository.findThemeOfActiveStore(themeId);
    theme.getStore().checkManager(user);

    theme.updateTheme(
        requestDto.getTitle(),
        requestDto.getContents(),
        requestDto.getLevel(),
        requestDto.getDuration(),
        requestDto.getMinPlayer(),
        requestDto.getMaxPlayer(),
        requestDto.getThemeType(),
        requestDto.getPrice()
    );

    themeRepository.save(theme);
    return new ThemeDetailResponseDto(theme);
  }

  /**
   * 방탈출 테마 이미지 수정
   *
   * @param themeId 테마 id
   * @param file    수정할 이미지 파일
   * @param user    로그인한 매니저
   * @return 수정한 이미지 경로
   */
  @Transactional
  public String modifyThemeImage(Long themeId, MultipartFile file, User user) {
    Theme theme = themeRepository.findThemeOfActiveStore(themeId);
    theme.getStore().checkManager(user);

    s3Uploader.deleteFileFromS3(theme.getThemeImage());
    String themeImage = s3Uploader.uploadThemeImage(file, theme.getStore().getId(), themeId);
    theme.updateThemeImage(themeImage);

    return themeImage;
  }

  /**
   * 방탈출 테마 이미지 삭제
   *
   * @param themeId 테마 id
   * @param user    로그인한 매니저
   */
  @Transactional
  public void deleteThemeImage(Long themeId, User user) {
    Theme theme = themeRepository.findThemeOfActiveStore(themeId);
    theme.getStore().checkManager(user);

    s3Uploader.deleteFileFromS3(theme.getThemeImage());
    theme.deleteThemeImage();
  }

  /**
   * 방탈출 테마 상태 변경
   *
   * @param themeId 테마 id
   * @param user    로그인한 매니저
   */
  @Transactional
  public void changeThemeStatus(Long themeId, User user) {
    Theme theme = themeRepository.findThemeOfActiveStore(themeId);
    theme.getStore().checkManager(user);

    theme.toggleThemeStatus();
  }

  /**
   * 방탈출 테마 완전히 삭제
   *
   * @param themeId 테마 id
   * @param user    로그인한 매니저
   */
  @Transactional
  public void deleteTheme(Long themeId, User user) {
    Theme theme = themeRepository.findThemeOfActiveStore(themeId);
    theme.getStore().checkManager(user);

    themeRepository.delete(theme);
  }
}

