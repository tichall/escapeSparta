package com.sparta.domain.recommendation.controller;

import com.sparta.domain.recommendation.service.RecommendationService;
import com.sparta.global.response.ResponseMessage;
import com.sparta.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/consumer/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * 좋아요 등록
     *
     * @param userDetails 로그인 유저
     * @param themeId     좋아요할 테마 id
     * @return status.code, message
     */
    @PostMapping("/theme/{themeId}")
    public ResponseEntity<ResponseMessage<Void>> createRecommendation(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long themeId) {

        recommendationService.createRecommendation(userDetails.getUser(), themeId);

        ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("방탈출 카페 테마를 좋아요 했습니다.")
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    /**
     * 좋아요 취소
     *
     * @param userDetails      로그인 유저
     * @param recommendationId 좋아요 id
     * @return status.code, message
     */
    @DeleteMapping("/{recommendationId}")
    public ResponseEntity<ResponseMessage<Void>> deleteRecommendation(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long recommendationId) {

        recommendationService.deleteRecommendation(userDetails.getUser(), recommendationId);

        ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("방탈출 카페 테마를 좋아요 취소 했습니다.")
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

}
