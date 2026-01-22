package edu.aivle.heatguard_ai_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Entity
public class GEELocationEntity {
    @Id @Column(name = "GEE_LOC_CD")
    private String gee_loc_cd;

    @Column(name = "GEE_LOC_LNG")
    private double gee_loc_lng;

    @Column(name = "GEE_LOC_LAT")
    private double gee_loc_lat;

    @Column(name = "ADDRESS_FULL")
    private String address_full;
}
