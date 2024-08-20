package com.sparta.controller;

import com.sparta.domain.review.dto.ReviewResponseDto;
import com.sparta.domain.review.service.ReviewService;
import com.sparta.domain.store.dto.StoreDetailResponseDto;
import com.sparta.domain.store.dto.StoreResponseDto;
import com.sparta.domain.store.dto.TopStoreResponseDto;
import com.sparta.domain.store.entity.StoreRegion;
import com.sparta.domain.store.service.StoreService;
import com.sparta.domain.theme.dto.ThemeInfoResponseDto;
import com.sparta.domain.theme.dto.ThemeResponseDto;
import com.sparta.domain.theme.dto.ThemeTimeResponseDto;
import com.sparta.domain.theme.service.ThemeService;
import com.sparta.global.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
@Slf4j
public class SearchController {

  private final StoreService storeService;
  private final ThemeService themeService;
  private final ReviewService reviewService;

  /**
   * 방탈출 카페 조회
   *
   * @param pageNum     페이지 번호
   * @param pageSize    페이지에 담는 데이터 수
   * @param isDesc      오름차순, 내림차순 정렬 기준
   * @param keyWord     검색 키워드
   * @param storeRegion 카페 지역
   * @param sort        속성별 정렬 기준
   * @return status.code, message, Store 리스트
   */
  @GetMapping("/stores")
  public ResponseEntity<ResponseMessage<Page<StoreResponseDto>>> getStores(
      @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
      @RequestParam(value = "isDesc", required = false, defaultValue = "false") boolean isDesc,
      @RequestParam(value = "keyWord", required = false) String keyWord,
      @RequestParam(value = "storeRegion", required = false, defaultValue = "ALL") StoreRegion storeRegion,
      @RequestParam(value = "sort", required = false, defaultValue = "name") String sort) {

    Page<StoreResponseDto> stores = storeService.getStores(pageNum, pageSize, isDesc, keyWord,
        storeRegion, sort);

    ResponseMessage<Page<StoreResponseDto>> responseMessage = ResponseMessage.<Page<StoreResponseDto>>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출 카페 조회에 성공했습니다.")
        .data(stores)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 방탈출 카페 테마 조회
   *
   * @param storeId  방탈출 카페 id
   * @param pageNum  페이지 번호
   * @param pageSize 페이지에 담는 데이터 수
   * @param isDesc   오름차순, 내림차순 정렬 기준
   * @param sort     속성별 정렬 기준
   * @return status.code, message, EscapeRoom 리스트
   */
  @GetMapping("/stores/{storeId}/theme")
  public ResponseEntity<ResponseMessage<Page<ThemeResponseDto>>> getTheme(
      @PathVariable Long storeId,
      @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
      @RequestParam(value = "isDesc", required = false, defaultValue = "false") boolean isDesc,
      @RequestParam(value = "sort", required = false, defaultValue = "title") String sort) {

    Page<ThemeResponseDto> themes = themeService.getTheme(storeId, pageNum, pageSize, isDesc, sort);

    ResponseMessage<Page<ThemeResponseDto>> responseMessage = ResponseMessage.<Page<ThemeResponseDto>>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출 카페 테마 조회에 성공했습니다.")
        .data(themes)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 방탈출 카페 테마 상세 조회
   *
   * @param themeId 해당 카페의 테마 id
   * @param storeId 카페 id
   * @return status.code, message, theme 반환
   */
  @GetMapping("/stores/theme/{themeId}/info")
  public ResponseEntity<ResponseMessage<ThemeInfoResponseDto>> getThemeInfo(
      @RequestParam(value = "storeId") Long storeId,
      @PathVariable Long themeId) {

    ThemeInfoResponseDto responseDto = themeService.getThemeInfo(storeId, themeId);

    ResponseMessage<ThemeInfoResponseDto> responseMessage = ResponseMessage.<ThemeInfoResponseDto>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출 카페 테마 조회에 성공했습니다")
        .data(responseDto)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 방탈출 카페 테마 시간 조회
   *
   * @param themeId 해당 카페의 테마 id
   * @param storeId 카페 id
   * @return status.code, message, theme 시간 반환
   */
  @GetMapping("/stores/theme/{themeId}/time")
  public ResponseEntity<ResponseMessage<List<ThemeTimeResponseDto>>> getThemeTime(
      @RequestParam(value = "storeId") Long storeId,
      @RequestParam(value = "day") String day,
      @PathVariable Long themeId) {

    List<ThemeTimeResponseDto> responseDtoList = themeService.getThemeTime(storeId, themeId, day);

    ResponseMessage<List<ThemeTimeResponseDto>> responseMessage = ResponseMessage.<List<ThemeTimeResponseDto>>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출 카페 테마 시간 조회에 성공했습니다.")
        .data(responseDtoList)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 방탈출 카페 테마 리뷰 조회
   *
   * @param storeId 방탈출 카페 id
   * @param themeId , 테마 id가 들어있는 dto
   * @return status.code, message, 리뷰 반환
   */
  @GetMapping("/reviews")
  public ResponseEntity<ResponseMessage<List<ReviewResponseDto>>> getReview(
      @RequestParam(value = "storeId") Long storeId,
      @RequestParam(value = "themeId") Long themeId) {

    List<ReviewResponseDto> reviewResponseDtoList = reviewService.getReview(storeId, themeId);

    ResponseMessage<List<ReviewResponseDto>> responseMessage = ResponseMessage.<List<ReviewResponseDto>>builder()
        .statusCode(HttpStatus.OK.value())
        .message("리뷰 조회에 성공했습니다.")
        .data(reviewResponseDtoList)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }


  /**
   * 인기 방탈출 카페 조회
   *
   * @return status.code, message, store 반환
   */
  @GetMapping("/top-stores")
  public ResponseEntity<ResponseMessage<TopStoreResponseDto>> getTopStores() {

    TopStoreResponseDto responseDto = storeService.getTopStores();

    ResponseMessage<TopStoreResponseDto> responseMessage = ResponseMessage.<TopStoreResponseDto>builder()
        .statusCode(HttpStatus.OK.value())
        .message("인기 방탈출 카페 조회에 성공했습니다.")
        .data(responseDto)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }

  /**
   * 방탈출 카페 상세 조회
   *
   * @param storeId 조회할 카페 id
   * @return status.code, message, storeInfo 반환
   */
  @GetMapping("/stores/{storeId}/info")
  public ResponseEntity<ResponseMessage<StoreDetailResponseDto>> getStoreInfo(
      @PathVariable Long storeId) {

    StoreDetailResponseDto responseDto = storeService.getStoreInfo(storeId);

    ResponseMessage<StoreDetailResponseDto> responseMessage = ResponseMessage.<StoreDetailResponseDto>builder()
        .statusCode(HttpStatus.OK.value())
        .message("방탈출 카페 상세 조회에 성공했습니다.")
        .data(responseDto)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
  }
}




