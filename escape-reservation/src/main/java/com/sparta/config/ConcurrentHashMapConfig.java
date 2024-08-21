package com.sparta.config;

import com.sparta.domain.reservation.dto.ReservationCreateResponseDto;
import com.sparta.domain.reservation.dto.ReservationResponseDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class ConcurrentHashMapConfig {
    @Bean
    public ConcurrentHashMap<String, CompletableFuture<ReservationCreateResponseDto>> ReservationResponseCreateFutures() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ConcurrentHashMap<String, CompletableFuture<Void>> ReservationResponseDeleteFutures() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ConcurrentHashMap<String, CompletableFuture<List<ReservationResponseDto>>> ReservationResponseGetFutures() {
        return new ConcurrentHashMap<>();
    }
}
