package edu.aivle.heatguard_ai_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "CF_MEASURE_TB")
public class CoolingFogMeasureEntity {

    @Id
    @Column(name = "CF_MEASURE_CD")
    private String cf_measure_cd;

    @Column(name = "CF_CD")
    private String cf_cd;

    @Column(name = "CF_MEASURE_DATE")
    private String cf_measure_date;

    @Column(name = "CF_MEASURE_HOUR")
    private String cf_measure_hour;

    @Column(name = "CF_MEASURE_SELECTED_TEMP")
    private Double cf_measure_selected_temp;

    @Column(name = "CF_MEASURE_NEARBY_TEMP")
    private Double cf_measure_nearby_temp;

    @Column(name = "CF_MEASURE_HUM_PER")
    private Double cf_measure_hum_per;
}
