package com.sparta.domain.store.dto.response;

import com.sparta.domain.store.entity.StoreStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreResponseDto {
    private final Long storeId;
    private final String name;
    private final String address;
    private final String phoneNumber;
    private final String workHours;
    private final String storeImage;
    private final StoreStatus storeStatus;

    @Builder
    public StoreResponseDto(com.sparta.domain.store.entity.Store store) {
        this.storeId = store.getId();
        this.name = store.getName();
        this.address = store.getAddress();
        this.phoneNumber = store.getPhoneNumber();
        this.workHours = store.getWorkHours();
        this.storeImage = store.getStoreImage();
        this.storeStatus = store.getStoreStatus();
    }
}

