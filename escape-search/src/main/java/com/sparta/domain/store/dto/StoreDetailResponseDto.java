package com.sparta.domain.store.dto;

import com.sparta.domain.store.entity.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreDetailResponseDto {

  private Long storeId;
  private String storeName;
  private String address;
  private String phoneNumber;
  private String workHours;
  private String storeImage;

  public StoreDetailResponseDto(Store store) {
    this.storeId = store.getId();
    this.storeName = store.getName();
    this.address = store.getAddress();
    this.phoneNumber = store.getPhoneNumber();
    this.workHours = store.getWorkHours();
    this.storeImage = store.getStoreImage();
  }
}
