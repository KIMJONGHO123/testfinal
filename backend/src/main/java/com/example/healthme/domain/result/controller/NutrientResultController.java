package com.example.healthme.domain.result.controller;

import com.example.healthme.domain.result.dto.NutrientResultDto;
import com.example.healthme.domain.result.service.NutrientResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/healthme/result")
public class NutrientResultController {

    private final NutrientResultService nutrientResultService;

    /**
     * 사용자의 영양소 점수를 분석하여, 퍼센트 및 설명 정보를 반환한다.
     * @param scoreMap Map<String, Integer> : { "탄수화물": 15, "단백질": 25, ... }
     * @return Map<String, NutrientResultDto> : 각 영양소별 분석 결과
     */
    @PostMapping("/summary")
    public ResponseEntity<Map<String, NutrientResultDto>> getSummary(@RequestBody Map<String, Integer> scoreMap) {
        return ResponseEntity.ok(nutrientResultService.summarize(scoreMap));
    }
}