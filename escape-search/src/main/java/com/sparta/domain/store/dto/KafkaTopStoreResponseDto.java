package com.sparta.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaTopStoreResponseDto {

  private String requestId;
  private TopStoreResponseDto responseDto;
}
