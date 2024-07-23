package com.sparta.controller;

import com.sparta.domain.user.entity.User;
import com.sparta.domain.user.repository.UserRepository;
import com.sparta.dto.request.StoreModifyRequestDto;
import com.sparta.dto.request.StoreRegisterRequestDto;
import com.sparta.dto.response.StoreModifyResponseDto;
import com.sparta.dto.response.StoresGetResponseDto;
import com.sparta.dto.response.StoreRegisterResponseDto;
import com.sparta.global.response.ResponseMessage;
import com.sparta.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final UserRepository userRepository;

    /**
     * 방탈출 카페 등록 요청
     * @param requestDto
     * @param manager
     * @return
     */
    @PostMapping
    public ResponseEntity<ResponseMessage<StoreRegisterResponseDto>> registerStore(@RequestBody StoreRegisterRequestDto requestDto, User manager) {

        StoreRegisterResponseDto responseDto = storeService.registerStore(requestDto, manager);

        ResponseMessage<StoreRegisterResponseDto> responseMessage = ResponseMessage.<StoreRegisterResponseDto>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("방탈출 카페 등록 요청이 완료되었습니다.")
                .data(responseDto)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    /**
     * 본인의 방탈출 카페 조회
     * @param manager
     * @return
     */
    @GetMapping
//    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ResponseMessage<StoresGetResponseDto>> getMyStore(User manager) {
        StoresGetResponseDto responseDto = storeService.getMyStore(manager);

        ResponseMessage<StoresGetResponseDto> responseMessage = ResponseMessage.<StoresGetResponseDto>builder()
                .statusCode(HttpStatus.OK.value())
                .message("본인의 방탈출 카페 조회가 완료되었습니다.")
                .data(responseDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    /**
     * 방탈출 카페 수정
     * @param storeId
     * @param requestDto
     * @param manager
     * @return
     */
    @PutMapping("/{storeId}")
    public ResponseEntity<ResponseMessage<StoreModifyResponseDto>> modifyStore(@PathVariable Long storeId, @RequestBody StoreModifyRequestDto requestDto, User manager) {
        StoreModifyResponseDto responseDto = storeService.modifyStore(storeId, requestDto, manager);

        ResponseMessage<StoreModifyResponseDto> responseMessage = ResponseMessage.<StoreModifyResponseDto>builder()
                .statusCode(HttpStatus.OK.value())
                .message("방탈출 카페 수정이 완료되었습니다.")
                .data(responseDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    /**
     * 방탈출 카페 삭제
     * @param storeId
     * @param user
     * @return
     */
    @DeleteMapping("/{storeId}")
    public ResponseEntity<ResponseMessage<Void>> deleteStore(@PathVariable Long storeId, User user) {
        storeService.deleteStore(storeId, user);

        ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("방탈출 카페 삭제가 완료되었습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

}
