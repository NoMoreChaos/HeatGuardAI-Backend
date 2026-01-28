package edu.aivle.heatguard_ai_back.dto.recommend.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecoLocResponse {

    @JsonProperty("result_address")
    private String resultAddress;

    @JsonProperty("result_count")
    private Integer resultCount;

    @JsonProperty("result")
    private List<RecoLocResultDto> result;
}
