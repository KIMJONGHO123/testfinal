package com.example.healthme.domain.result.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NutrientResultDto {
    private int percent;
    private String description;
    private String color;
}