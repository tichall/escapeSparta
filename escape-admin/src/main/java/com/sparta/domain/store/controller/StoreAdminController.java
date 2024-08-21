package com.sparta.domain.store.controller;

import com.sparta.domain.store.dto.request.StoreCreateRequestDto;
import com.sparta.domain.store.dto.request.StoreModifyRequestDto;
import com.sparta.domain.store.dto.response.StoreDetailResponseDto;
import com.sparta.domain.store.dto.response.StoreResponseDto;
import com.sparta.domain.store.service.StoreAdminService;
import com.sparta.global.response.ResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class StoreAdminController {

  private final StoreAdminService storeAdminService;

  /**
   * 방탈출 카페 강제 등록
   *
   * @param file       카페 이미지 파일
   * @param requestDto 등록할 카페 정보 Dto
   * @return status.code, message, 등록한 카페 정보 반환
   */
  @PostMapping("/stores")
  public ResponseEntity<ResponseMessage<StoreDetailResponseDto>> createStoreByAdmin(
      @RequestPart(value = "file", required = false) MultipartFile file,
      @Valid @RequestPart StoreCreateRequestDto requestDto
  ) {

    StoreDetailResponseDto responseDto = storeAdminService.createStoreByAdmin(file, requestDto);

    ResponseMessage<StoreDetailResponseDto> responseMessage = ResponseMessage.<StoreDetailResponseDto>builder()
        .statusCode(HttpStatus.CREATED.value())
        .message("관리자에 의해 방탈출 카페가 강제 등록 되었습니다.")
        .data(responseDto)
        .build();

    return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
  }

  /**
   * 모든 방탈출 카페 조회 (모든 상태: 대기중, 활성화, 비활성화)
   *
   * @return status.code, message, 조회한 모든 카페 리스트 반환
   */
  @GetMapping("/stores")
  public ResponseEntity<ResponseMessage<List<StoreResponseDto>>> getAllStore() {

    List<StoreResponseDto> responseDto = storeAdminService.getAllStore();

    ResponseMessage<List<StoreResponseDto>> responseMessage = ResponseMessage.<List<StoreResponseDto>>builder()
        .statusCode(HttpStatus.OK.value())
        .data(responseDto)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 방탈출 카페 등록 승인 ( PENDING -> ACTIVE )
   *
   * @param storeId 카페 id
   * @return status.code, message
   */
  @PutMapping("/stores/{storeId}/approval")
  public ResponseEntity<ResponseMessage<Void>> approveStore(@Valid @PathVariable Long storeId) {

    storeAdminService.approveStore(storeId);

    ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출카페가 등록 되었습니다.")
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 방탈출 카페 수정
   *
   * @param storeId    방탈출 카페 id
   * @param requestDto 수정할 카페 정보 Dto
   * @return status.code, message, 수정한 카페 정보 반환
   */
  @PutMapping("/{storeId}")
  public ResponseEntity<ResponseMessage<StoreDetailResponseDto>> modifyStore(
      @PathVariable Long storeId,
      @Valid @RequestBody StoreModifyRequestDto requestDto
  ) {
    StoreDetailResponseDto responseDto = storeAdminService.modifyStore(storeId, requestDto);

    ResponseMessage<StoreDetailResponseDto> responseMessage = ResponseMessage.<StoreDetailResponseDto>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출 카페 수정이 완료되었습니다.")
        .data(responseDto)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 방탈출 카페 이미지 수정
   *
   * @param storeId 카페 id
   * @param file    수정할 이미지 파일
   * @return status.code, message, 수정한 이미지 경로 반환
   */
  @PutMapping("/{storeId}/image")
  public ResponseEntity<ResponseMessage<String>> modifyStoreImage(
      @PathVariable Long storeId,
      @RequestPart(value = "file", required = false) MultipartFile file
  ) {
    String imagePath = storeAdminService.modifyStoreImage(storeId, file);

    ResponseMessage<String> responseMessage = ResponseMessage.<String>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출 카페 이미지 수정이 완료되었습니다.")
        .data(imagePath)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 방탈출 카페 이미지 삭제
   *
   * @param storeId 카페 id
   * @return status.code, message
   */
  @DeleteMapping("/{storeId}/image")
  public ResponseEntity<ResponseMessage<Void>> deleteStoreImage(
      @PathVariable Long storeId
  ) {
    storeAdminService.deleteStoreImage(storeId);

    ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출 카페 이미지 삭제가 완료되었습니다.")
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 방탈출 카페 삭제
   *
   * @param storeId 카페 id
   * @return status.code, message
   */
  @DeleteMapping("/{storeId}")
  public ResponseEntity<ResponseMessage<Void>> deactivateStore(
      @PathVariable Long storeId
  ) {
    storeAdminService.deactivateStore(storeId);

    ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출 카페 비활성화가 완료되었습니다.")
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 방탈출 카페 완전 삭제
   *
   * @param storeId 카페 id
   * @return status.code, message
   */
  @DeleteMapping
  public ResponseEntity<ResponseMessage<Void>> deleteStore(@Valid @RequestBody Long storeId) {

    storeAdminService.deleteStore(storeId);

    ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
        .statusCode(HttpStatus.OK.value())
        .message("해당 방탈출 카페가 삭제 되었습니다.")
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }
}
