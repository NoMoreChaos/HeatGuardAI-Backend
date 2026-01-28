package edu.aivle.heatguard_ai_back.controller;

import edu.aivle.heatguard_ai_back.dto.ApiResponse;
import edu.aivle.heatguard_ai_back.dto.coolingfog.response.CoolingFogDetailResponse;
import edu.aivle.heatguard_ai_back.dto.coolingfog.response.CoolingFogListResponse;
import edu.aivle.heatguard_ai_back.service.CoolingFogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cf")
public class CoolingFogController {

    private final CoolingFogService coolingFogService;

    // 쿨링포그 전체 리스트
    @GetMapping("/list")
    public ApiResponse<CoolingFogListResponse> getCoolingFogList() {
        return ApiResponse.success(coolingFogService.getCoolingFogList());
    }

    // 쿨링포그 상세
    @GetMapping("/{cf_cd}")
    public ApiResponse<CoolingFogDetailResponse> getCoolingFogDetail(@PathVariable("cf_cd") String cf_cd) {
        return ApiResponse.success(coolingFogService.getCoolingFogDetail(cf_cd));
    }
}
