package edu.aivle.heatguard_ai_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "GEE_LOC_TB")
public class GEELocEntity {

    @Id
    @Column(name = "GEE_LOC_CD", length = 36, nullable = false)
    private String geeLocCd;

    @Column(name = "GEE_LOC_LNG", nullable = false)
    private Double geeLocLng;

    @Column(name = "GEE_LOC_LAT", nullable = false)
    private Double geeLocLat;

    @Column(name = "GEE_ADDRESS_FULL", length = 255)
    private String geeAddressFull;

    @Column(name = "GEE_CITY_GU", length = 255)
    private String geeCityGu;

    @Column(name = "GEE_CITY_DONG", length = 255)
    private String geeCityDong;
}
