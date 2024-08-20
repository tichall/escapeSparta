package com.sparta.domain.review.controller;

import com.sparta.domain.review.dto.*;
import com.sparta.domain.review.service.ReviewService;
import com.sparta.global.response.ResponseMessage;
import com.sparta.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/consumer")
public class ReviewController {

    private final ReviewService reviewService;


    /**
     * 테마 리뷰 등록
     *
     * @param reviewCreateRequestDto 작성할 리뷰의 데이터 값
     * @param userDetails            로그인 유저
     * @return status.code, message, 작성한 리뷰
     */
    @PostMapping("/reviews")
    public ResponseEntity<ResponseMessage<ReviewCreateResponseDto>> createReview(
            @Valid @RequestBody ReviewCreateRequestDto reviewCreateRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ReviewCreateResponseDto reviewCreateResponseDto = reviewService.createReview(
                reviewCreateRequestDto, userDetails.getUser());

        ResponseMessage<ReviewCreateResponseDto> responseMessage = ResponseMessage.<ReviewCreateResponseDto>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("리뷰 등록 성공!")
                .data(reviewCreateResponseDto)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    /**
     * 테마 리뷰 수정
     *
     * @param reviewId               수정할 리뷰의 id
     * @param reviewUpdateRequestDto 수정할 리뷰의 데이터 값
     * @param userDetails            로그인 유저
     * @return status.code, message, ReviewUpdateResponseDto(수정한 리뷰)
     */
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ResponseMessage<ReviewUpdateResponseDto>> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateRequestDto reviewUpdateRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ReviewUpdateResponseDto reviewUpdateResponseDto = reviewService.updateReview(reviewId,
                reviewUpdateRequestDto,
                userDetails.getUser());

        ResponseMessage<ReviewUpdateResponseDto> responseMessage = ResponseMessage.<ReviewUpdateResponseDto>builder()
                .statusCode(HttpStatus.OK.value())
                .message("리뷰 수정 성공!")
                .data(reviewUpdateResponseDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    /**
     * 리뷰 조회
     *
     * @param reviewId 조회할 리뷰 id
     * @return status.code, message, ReviewResponseDto(리뷰 정보)
     */
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ResponseMessage<ReviewResponseDto>> getReview(
            @PathVariable Long reviewId) {
        ReviewResponseDto responseDto = reviewService.getReview(reviewId);

        ResponseMessage<ReviewResponseDto> responseMessage = ResponseMessage.<ReviewResponseDto>builder()
                .statusCode(HttpStatus.OK.value())
                .message("리뷰 조회 성공!")
                .data(responseDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    /**
     * 테마 리뷰 삭제
     *
     * @param reviewId    수정할 리뷰의 id
     * @param userDetails 로그인 유저
     * @return status.code, message
     */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ResponseMessage<Void>> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        reviewService.deleteReview(reviewId, userDetails.getUser());

        ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("리뷰 삭제 성공!")
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    /**
     * 리액션 등록, 수정, 삭제
     *
     * @param reviewId        리액션할 리뷰 id
     * @param reactionRequest 리액션 종류
     * @param userDetails     로그인 유저
     * @return status.code, message, ReactionResponseDto(리액션 정보)
     */
    @PostMapping("/reviews/{reviewId}/reaction")
    public ResponseEntity<ResponseMessage<ReactionResponseDto>> createReaction(
            @PathVariable Long reviewId,
            @RequestBody ReactionRequest reactionRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReactionResponseDto reactionResponseDto = reviewService.createReaction(reviewId,
                reactionRequest.getReactionType(), userDetails.getUser());

        String message = reactionResponseDto.getReactionStatus() ? "리액션 등록 성공!" : "리액션 취소 성공!";

        ResponseMessage<ReactionResponseDto> responseMessage = ResponseMessage.<ReactionResponseDto>builder()
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .data(reactionResponseDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    /**
     * 내가 등록한 리뷰 전체 보기
     *
     * @param userDetail 로그인 유저
     * @return status.code, message, ReviewResponseDto(등록한 리뷰)
     */
    @GetMapping("/mypage/reviews")
    public ResponseEntity<ResponseMessage<List<ReviewResponseDto>>> getMyReviews(
            @AuthenticationPrincipal UserDetailsImpl userDetail) {

        List<ReviewResponseDto> reviewResponseDtoList = reviewService.getMyReviews(
                userDetail.getUser());

        ResponseMessage<List<ReviewResponseDto>> responseMessage = ResponseMessage.<List<ReviewResponseDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("리뷰 조회 성공!")
                .data(reviewResponseDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }


}
