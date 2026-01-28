package edu.aivle.heatguard_ai_back.dto.coolingfog.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class CoolingFogDetailResponse {
    private String cf_city_gu;
    private String cf_city_dong;
    private String cf_location;
    private String cf_address;
    private double lat;
    private double lng;
    private boolean cf_state;

    // cf_measure_tb
    private Double cf_selected_temp;
    private Double cf_nearby_temp;
    private Double cf_hum_per;

    private String cf_inst_date;
    private String cf_manage_dept;
    private String cf_manager_nm;
    private String cf_manager_contact;

    private Map<String, TimeMeasure> time;

    @Getter
    @AllArgsConstructor
    public static class TimeMeasure {
        private Double cf_selected_temp;
        private Double cf_nearby_temp;
        private Double cf_hum_per;
    }
}
