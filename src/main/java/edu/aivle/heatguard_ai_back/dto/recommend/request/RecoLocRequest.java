package edu.aivle.heatguard_ai_back.dto.recommend.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecoLocRequest {

    @JsonProperty("target_count")
    private Integer targetCount; // 1~5

    @JsonProperty("target_region_gu")
    private String targetRegionGu; // null 가능

    @JsonProperty("target_region_dong")
    private String targetRegionDong; // null 가능

    @JsonProperty("reco_loc_type_cd")
    private Integer recoLocTypeCd; // 1~3
}
