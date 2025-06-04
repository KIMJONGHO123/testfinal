package com.example.healthme.domain.result.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nutrient_result_desc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutrientResultDesc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nutrientName;

    // 설명을 보여줄 기준 퍼센트 값 (ex. 0, 20, 40...)
    private int rangeStart;

    @Column(columnDefinition = "TEXT")
    private String description;
}