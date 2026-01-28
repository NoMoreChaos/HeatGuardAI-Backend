package edu.aivle.heatguard_ai_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Entity(name = "CF_TB")
public class CoolingFogEntity {
    @Id @Column(name = "CF_CD")
    private String cf_cd;

    @Column(name = "CF_CITY")
    private String cf_city;

    @Column(name = "CF_CITY_GU")
    private String cf_city_gu;

    @Column(name = "CF_CITY_DONG")
    private String cf_city_dong;

    @Column(name = "CF_LOCATION")
    private String cf_location;

    @Column(name = "CF_ADDRESS")
    private String cf_address;

    @Column(name = "CF_LAT")
    private double cf_lat;

    @Column(name = "CF_LNG")
    private double cf_lng;

    @Column(name = "CF_STATE")
    private boolean cf_state;

    @Column(name = "CF_INST_DATE")
    private String cf_inst_date;

    @Column(name = "CF_MANAGE_DEPT")
    private String cf_manage_dept;

    @Column(name = "CF_MANAGER_NM")
    private String cf_manager_nm;

    @Column(name = "CF_MANAGER_CONTACT")
    private String cf_manager_contact;
}
