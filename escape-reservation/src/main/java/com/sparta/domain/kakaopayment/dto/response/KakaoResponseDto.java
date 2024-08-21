package com.sparta.domain.kakaopayment.dto.response;

public class KakaoResponseDto {

  public String tid;
  public String next_redirect_pc_url;

  public KakaoResponseDto(String tid, String next_redirect_pc_url) {
    this.tid = tid;
    this.next_redirect_pc_url = next_redirect_pc_url;
  }
}
