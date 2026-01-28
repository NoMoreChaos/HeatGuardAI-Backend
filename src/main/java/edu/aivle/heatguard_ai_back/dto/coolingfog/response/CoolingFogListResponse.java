package edu.aivle.heatguard_ai_back.dto.coolingfog.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CoolingFogListResponse {
    private int totalCount;
    private List<CoolingFog> cfList;

    @Getter
    @AllArgsConstructor
    public static class CoolingFog {
        private String cf_cd;
        private double lat;
        private double lng;
    }
}
