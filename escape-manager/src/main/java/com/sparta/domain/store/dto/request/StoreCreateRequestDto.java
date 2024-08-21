package com.sparta.domain.store.dto.request;

import com.sparta.domain.store.entity.StoreRegion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StoreCreateRequestDto {

    @NotBlank(message = "이름을 적어주세요.")
    private String name;
    @NotBlank(message = "주소를 적어주세요.")
    private String address;
    @NotBlank(message = "전화번호를 적어주세요.")
    private String phoneNumber;
    @NotBlank(message = "오픈 시간을 적어주세요.")
    private String workHours;

    @NotNull(message = "방탈출 카페 위치 지역은 필수값입니다.")
    private StoreRegion storeRegion;

    @NotNull
    private Long managerId;

}
