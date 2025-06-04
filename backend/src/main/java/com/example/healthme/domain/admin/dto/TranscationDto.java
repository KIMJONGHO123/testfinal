package com.example.healthme.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranscationDto {
    // 거래내역 수정 할때 사용하는 DTO

    private String productName;

    private int productPrice;

    private String transcationPeople;

    private String transcationBank;

    private LocalDateTime transcationTime;

    private String cancel;

    private String success;
}
