package com.sparta.domain.store.dto.response;

import com.sparta.domain.store.entity.Store;
import com.sparta.domain.store.entity.StoreRegion;
import com.sparta.domain.store.entity.StoreStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StoreRegisterResponseDto {
    private final Long storeId;
    private final String name;
    private final String address;
    private final String phoneNumber;
    private final String workHours;
    private final String storeImage;
    private final StoreRegion storeRegion;
    private final StoreStatus storeStatus;
    private final Long managerId;
    private final String managerName;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;


    public StoreRegisterResponseDto(Store store) {
        this.storeId = store.getId();
        this.name = store.getName();
        this.address = store.getAddress();
        this.phoneNumber = store.getPhoneNumber();
        this.workHours = store.getWorkHours();
        this.storeImage = store.getStoreImage();
        this.storeRegion = store.getStoreRegion();
        this.storeStatus = store.getStoreStatus();
        this.managerId = store.getManager().getId();
        this.managerName = store.getManager().getName();
        this.createdAt = store.getCreatedAt();
        this.updatedAt = store.getUpdatedAt();
    }

//    public static StoreCreateResponseDto of(Store store) {
//        return StoreCreateResponseDto.builder()
//                .id(store.getId())
//                .name(store.getName())
//                .address(store.getAddress())
//                .phoneNumber(store.getPhoneNumber())
//                .workHours(store.getWorkHours())
//                .storeImage(store.getStoreImage())
//                .managerId(store.getManager().getId())
//                .managerName(store.getManager().getName())
//                .build();
//    }
}
