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
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/reservations/{reservationId}")
    public KakaoResponseDto preparePayment(@PathVariable Long reservationId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return paymentService.preparePayment(reservationId);
    }


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

//    @GetMapping("/kakaopay-fail")
//    public ResponseEntity<ResponseMessage<Void>> kakaoPaySuccess() {
//
//        ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
//                .statusCode(HttpStatus.CREATED.value())
//                .message("예약에 실패했습니다.")
//                .data(null)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
//    }


}