package com.example.healthme.domain.result.service;

import com.example.healthme.domain.result.dto.NutrientResultDto;
import com.example.healthme.domain.result.entity.NutrientResultDesc;
import com.example.healthme.domain.result.repository.NutrientResultDescRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NutrientResultService {

    private final NutrientResultDescRepository descRepository;

    private final Map<String, String> nutrientColorMap = Map.of(
            "탄수화물", "blue",
            "단백질", "yellow",
            "지방", "green",
            "비타민", "purple",
            "아이오딘", "red",
            "철분", "teal"
    );

    public Map<String, NutrientResultDto> summarize(Map<String, Integer> scoreMap) {
        Map<String, NutrientResultDto> result = new LinkedHashMap<>();
        int maxScore = 30;

        for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
            String nutrient = entry.getKey();
            int score = entry.getValue();

            int percent = Math.min(100, Math.round(score * 100f / maxScore));
            int rangeStart = (percent / 20) * 20;

            String description = descRepository
                    .findByNutrientAndRange(nutrient, rangeStart)
                    .map(NutrientResultDesc::getDescription)
                    .orElse("설명을 찾을 수 없습니다.");

            NutrientResultDto dto = new NutrientResultDto(
                    percent,
                    description,
                    nutrientColorMap.getOrDefault(nutrient, "gray")
            );

            result.put(nutrient, dto);
        }

        return result;
    }
}