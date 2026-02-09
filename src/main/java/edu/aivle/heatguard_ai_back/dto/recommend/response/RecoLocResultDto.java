package edu.aivle.heatguard_ai_back.dto.recommend.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

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

    @JsonProperty("gee_address_full")
    private String geeAddressFull; // 전체 주소

    @JsonProperty("reco_loc_popu_level")
    private String recoLocPopuLevel; // 유동인구 수준

    @JsonProperty("reco_loc_vulnerable_level")
    private String recoLocVulnerableLevel; // 취약계층 점수수준

    @JsonProperty("reco_loc_feel_temp")
    private BigDecimal recoLocFeelTemp; // 체감온도

    @JsonProperty("reco_loc_lst_level")
    private String recoLocLstLevel; // 지표면온도 점수수준

    @JsonProperty("reco_loc_ndvi_level")
    private String recoLocNdviLevel; // 식생지수 점수수준

    @JsonProperty("reco_loc_total_score")
    private Integer recoLocTotalScore; // 종합점수

    @JsonProperty("reco_loc_desc")
    private List<String> recoLocDesc; // 추천 사유
}
