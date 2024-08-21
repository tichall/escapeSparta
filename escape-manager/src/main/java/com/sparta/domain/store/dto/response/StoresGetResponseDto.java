package com.sparta.domain.store.dto.response;

import com.sparta.domain.store.entity.Store;
import java.util.List;
import lombok.Getter;

@Getter
public class StoresGetResponseDto {

  private final int totalStore;
  private List<StoreDetailResponseDto> storeDtoList;

  public StoresGetResponseDto(List<Store> storeList) {
    this.totalStore = storeList.size();
    this.storeDtoList = storeList.stream().map(StoreDetailResponseDto::new).toList();
  }
}
