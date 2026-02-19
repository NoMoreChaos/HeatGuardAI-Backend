package edu.aivle.heatguard_ai_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "CF_ITEM_TB")
public class CoolingFogItemEntity {

    @Id
    @Column(name = "CF_ITEM_CD")
    private String cf_item_cd;

    @Column(name = "CF_ITEM_NM")
    private String cf_item_nm;

    @Column(name = "CF_ITEM_PRICE")
    private int cf_item_price;

    @Column(name = "CF_ITEM_ELEC_COST")
    private int cf_item_elec_cost;

    @Column(name = "CF_ITEM_WATER_COST")
    private int cf_item_water_cost;

    @Column(name = "CF_ITEM_DESC")
    private String cf_item_desc;

    @Column(name = "CF_ITEM_BUY_URL")
    private String cf_item_buy_url;
}
