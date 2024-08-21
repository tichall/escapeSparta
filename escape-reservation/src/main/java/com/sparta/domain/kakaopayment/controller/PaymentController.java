package com.sparta.domain.kakaopayment.controller;


import com.sparta.domain.kakaopayment.dto.request.PaymentCreateRequestDto;
import com.sparta.domain.kakaopayment.dto.response.KakaoResponseDto;
import com.sparta.domain.kakaopayment.dto.response.PaymentResponseDto;
import com.sparta.domain.kakaopayment.service.PaymentService;
import com.sparta.global.response.ResponseMessage;
import com.sparta.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 카카오 페이 결제진행할 url
     *
     * @param reservationId 예약 id
     * @param userDetails   로그인한 유저 정보
     * @return KakaoResponseDto tid 결제고유번호
     * next_redirect_pc_url : 결제할수있는 url
     */
    @PostMapping("/{reservationId}/payments")
    public KakaoResponseDto preparePayment(@PathVariable Long reservationId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return paymentService.preparePayment(reservationId);
    }

    /**
     * 카카오 페이 환불
     *
     * @param reservationId 예약 id
     * @param userDetails   로그인한 유저 정보
     * @return status.code, message
     */
    @DeleteMapping("/{reservationId}/payments")
    public ResponseEntity<ResponseMessage<Void>> refundPayment(@PathVariable Long reservationId,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {

        paymentService.refundPayment(reservationId);

        ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("환불에 성공했습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    /**
     * 카카오 페이 결제완료시 payment에 데이터 저장
     *
     * @param requestDto  : reservationId - 예약 id, tid - 결제고유번호
     * @param userDetails 로그인한 유저 정보
     * @return status.code, message , PaymentResponseDto 결제정보
     */
    @PostMapping("/kakaopay-success")
    public ResponseEntity<ResponseMessage<PaymentResponseDto>> kakaoPaySuccess(
            @RequestBody PaymentCreateRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PaymentResponseDto responseDto = paymentService.kakaoPaySuccess(requestDto);

        ResponseMessage<PaymentResponseDto> responseMessage = ResponseMessage.<PaymentResponseDto>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("예약에 성공했습니다.")
                .data(responseDto)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }


}