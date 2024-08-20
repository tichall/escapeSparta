package com.sparta.domain.store.service;

import com.sparta.domain.s3.S3Uploader;
import com.sparta.domain.store.dto.request.StoreModifyRequestDto;
import com.sparta.domain.store.dto.request.StoreRegisterRequestDto;
import com.sparta.domain.store.dto.response.StoreDetailResponseDto;
import com.sparta.domain.store.dto.response.StoreRegisterResponseDto;
import com.sparta.domain.store.dto.response.StoresGetResponseDto;
import com.sparta.domain.store.entity.Store;
import com.sparta.domain.store.entity.StoreStatus;
import com.sparta.domain.store.repository.StoreRepository;
import com.sparta.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;
  private final S3Uploader s3Uploader;

  /**
   * 방탈출 카페 등록 요청
   *
   * @param file       카페 이미지 파일
   * @param requestDto 등록할 Store 정보 Dto
   * @param user       로그인한 매니저
   * @return 등록 요청한 카페 정보
   */
  @Transactional
  public StoreRegisterResponseDto registerStore(MultipartFile file,
      StoreRegisterRequestDto requestDto, User user) {
    Store store = Store.builder()
        .name(requestDto.getName())
        .address(requestDto.getAddress())
        .phoneNumber(requestDto.getPhoneNumber())
        .workHours(requestDto.getWorkHours())
        .manager(user)
        .storeRegion(requestDto.getStoreRegion())
        .storeStatus(StoreStatus.PENDING)
        .build();

    storeRepository.save(store);

    String storeImage = s3Uploader.uploadStoreImage(file, store.getId());
    store.updateStoreImage(storeImage);

    return new StoreRegisterResponseDto(store);
  }

  /**
   * 본인의 방탈출 카페 조회
   *
   * @param user 로그인한 매니저
   * @return 조회한 카페 리스트
   */
  public StoresGetResponseDto getMyStore(User user) {
    List<Store> storeList = storeRepository.findAllByManagerId(user.getId());
    return new StoresGetResponseDto(storeList);
  }

  /**
   * 방탈출 카페 수정
   *
   * @param storeId    방탈출 카페 id
   * @param requestDto 수정할 카페 정보 Dto
   * @param user       로그인한 매니저
   * @return 수정한 카페 정보
   */
  @Transactional
  public StoreDetailResponseDto modifyStore(Long storeId, StoreModifyRequestDto requestDto,
      User user) {
    Store store = storeRepository.findByActiveStore(storeId);
    store.checkManager(user);

    store.updateStore(
        requestDto.getName(),
        requestDto.getAddress(),
        requestDto.getPhoneNumber(),
        requestDto.getWorkHours(),
        requestDto.getStoreRegion()
    );

    storeRepository.save(store);
    return new StoreDetailResponseDto(store);
  }

  /**
   * 방탈출 카페 이미지 수정
   *
   * @param storeId 카페 id
   * @param file    수정할 이미지 파일
   * @param user    로그인한 매니저
   * @return 수정한 이미지 경로
   */
  @Transactional
  public String modifyStoreImage(Long storeId, MultipartFile file, User user) {
    Store store = storeRepository.findByActiveStore(storeId);
    store.checkManager(user);

    s3Uploader.deleteFileFromS3(store.getStoreImage());
    String storeImage = s3Uploader.uploadStoreImage(file, store.getId());
    store.updateStoreImage(storeImage);

    return storeImage;
  }

  /**
   * 방탈출 카페 이미지 삭제
   *
   * @param storeId 카페 id
   * @param user    로그인한 매니저
   */
  @Transactional
  public void deleteStoreImage(Long storeId, User user) {
    Store store = storeRepository.findByActiveStore(storeId);
    store.checkManager(user);

    s3Uploader.deleteFileFromS3(store.getStoreImage());
    store.deleteStoreImage();
  }

  /**
   * 방탈출 카페 삭제
   *
   * @param storeId 카페 id
   * @param user    로그인한 매니저
   */
  @Transactional
  public void deleteStore(Long storeId, User user) {
    Store store = storeRepository.findByActiveStore(storeId);
    store.checkManager(user);
    store.deactivateStore();
  }
}