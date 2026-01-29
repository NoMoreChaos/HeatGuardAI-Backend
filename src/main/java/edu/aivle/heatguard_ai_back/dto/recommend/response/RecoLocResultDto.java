package edu.aivle.heatguard_ai_back.dto.recommend.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecoLocResultDto {

    @JsonProperty("lat")
    private Double lat; // 위도

    @JsonProperty("lng")
    private Double lng; // 경도

    @JsonProperty("reco_loc_rank")
    private Integer recoLocRank;

    @JsonProperty("gee_loc_address")
    private String geeLocAddress; // 주소(풀주소)

    @JsonProperty("reco_loc_risk")
    private Double recoLocRisk; // 위험도

    @JsonProperty("reco_loc_desc")
    private String recoLocDesc; // 설명(근거)
}
