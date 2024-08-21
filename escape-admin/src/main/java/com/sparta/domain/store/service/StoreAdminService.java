package com.sparta.domain.store.service;

import com.sparta.domain.s3.S3Uploader;
import com.sparta.domain.store.dto.request.StoreCreateRequestDto;
import com.sparta.domain.store.dto.request.StoreModifyRequestDto;
import com.sparta.domain.store.dto.response.StoreDetailResponseDto;
import com.sparta.domain.store.dto.response.StoreResponseDto;
import com.sparta.domain.store.entity.Store;
import com.sparta.domain.store.entity.StoreStatus;
import com.sparta.domain.store.repository.StoreRepository;
import com.sparta.domain.user.entity.User;
import com.sparta.domain.user.repository.UserRepository;
import com.sparta.global.exception.customException.StoreException;
import com.sparta.global.exception.errorCode.StoreErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreAdminService {

  private final StoreRepository storeRepository;
  private final UserRepository userRepository;
  private final S3Uploader s3Uploader;

  /**
   * 방탈출 카페 강제 등록
   *
   * @param requestDto 등록할 카페 정보 Dto
   * @return 등록한 카페 정보
   */
  @Transactional
  public StoreDetailResponseDto createStoreByAdmin(MultipartFile file,
      StoreCreateRequestDto requestDto) {

    User manager = userRepository.findByIdOrElseThrow(requestDto.getManagerId());

    Store store = Store.builder()
        .name(requestDto.getName())
        .address(requestDto.getAddress())
        .phoneNumber(requestDto.getPhoneNumber())
        .workHours(requestDto.getWorkHours())
        .storeRegion(requestDto.getStoreRegion())
        .manager(manager)
        .storeStatus(StoreStatus.ACTIVE)
        .build();

    storeRepository.save(store);
    String storeImage = s3Uploader.uploadStoreImage(file, store.getId());
    store.updateStoreImage(storeImage);

    return new StoreDetailResponseDto(store);
  }

  /**
   * 모든 방탈출 카페 조회 (모든 상태: 대기중, 활성화, 비활성화)
   *
   * @return 조회한 모든 카페 리스트
   */
  @Transactional(readOnly = true)
  public List<StoreResponseDto> getAllStore() {
    List<Store> stores = storeRepository.findAll();

    return stores.stream()
        .map(StoreResponseDto::new)
        .collect(Collectors.toList());
  }

  /**
   * 방탈출 카페 등록 승인
   *
   * @param storeId 방탈출 카페 id
   */
  @Transactional
  public void approveStore(Long storeId) {
    Store store = storeRepository.findByIdOrElseThrow(storeId);

    if (store.getStoreStatus() == StoreStatus.PENDING) {
      store.setStoreStatus(StoreStatus.ACTIVE);
    } else {
      throw new StoreException(StoreErrorCode.STORE_ALREADY_EXIST);
    }
  }

  /**
   * 방탈출 카페 수정
   *
   * @param storeId    방탈출 카페 id
   * @param requestDto 수정할 카페 정보 Dto
   * @return 수정한 카페 정보
   */
  @Transactional
  public StoreDetailResponseDto modifyStore(Long storeId, StoreModifyRequestDto requestDto) {
    Store store = storeRepository.findByActiveStore(storeId);

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
   * @return 수정한 이미지 경로
   */
  @Transactional
  public String modifyStoreImage(Long storeId, MultipartFile file) {
    Store store = storeRepository.findByActiveStore(storeId);
    s3Uploader.deleteFileFromS3(store.getStoreImage());
    String storeImage = s3Uploader.uploadStoreImage(file, store.getId());
    store.updateStoreImage(storeImage);

    return storeImage;
  }

  /**
   * 방탈출 카페 이미지 삭제
   *
   * @param storeId 카페 id
   */
  @Transactional
  public void deleteStoreImage(Long storeId) {
    Store store = storeRepository.findByActiveStore(storeId);
    s3Uploader.deleteFileFromS3(store.getStoreImage());
    store.deleteStoreImage();
  }

  /**
   * 방탈출 카페 삭제
   *
   * @param storeId 카페 id
   */
  @Transactional
  public void deactivateStore(Long storeId) {
    Store store = storeRepository.findByActiveStore(storeId);
    store.deactivateStore();
  }

  /**
   * 방탈출 카페 완전 삭제
   *
   * @param storeId 카페 id
   */
  @Transactional
  public void deleteStore(Long storeId) {
    Store store = storeRepository.findByIdOrElseThrow(storeId);
    storeRepository.delete(store);
  }

  /**
   * 방탈출 카페 복구
   *
   * @param storeId 카페 id
   */
  @Transactional
  public void activateStore(Long storeId) {
    Store store = storeRepository.findByIdOrElseThrow(storeId);
    store.activateStore();
  }

}
