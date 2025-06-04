package com.example.healthme.domain.result.repository;


import com.example.healthme.domain.result.entity.NutrientResultDesc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NutrientResultDescRepository extends JpaRepository<NutrientResultDesc, Long> {

    @Query("SELECT d FROM NutrientResultDesc d WHERE d.nutrientName = :nutrient AND :score BETWEEN d.rangeStart AND d.rangeStart + 19")
    Optional<NutrientResultDesc> findByNutrientAndRange(@Param("nutrient") String nutrient, @Param("score") int score);

}
