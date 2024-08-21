package com.sparta.domain.store.dto;

import com.sparta.domain.store.entity.Store;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TopStoreResponseDto {

  private List<StoreResponseDto> responseDtoList;

  public TopStoreResponseDto(List<Store> storeList) {
    this.responseDtoList = storeList.stream().map(StoreResponseDto::new).toList();
  }
}
