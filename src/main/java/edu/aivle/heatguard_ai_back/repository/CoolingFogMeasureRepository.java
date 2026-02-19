package edu.aivle.heatguard_ai_back.repository;

import edu.aivle.heatguard_ai_back.entity.CoolingFogMeasureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoolingFogMeasureRepository extends JpaRepository<CoolingFogMeasureEntity, String> {

    @Query("""
                SELECT m
                FROM CF_MEASURE_TB m
                WHERE m.cf_cd = :cf_cd
                  AND m.cf_measure_hour <= :hour
                ORDER BY m.cf_measure_hour ASC
            """)
    List<CoolingFogMeasureEntity> findMeasures(
            @Param("cf_cd") String cf_cd,
            @Param("hour") String hour
    );
}
