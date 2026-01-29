package edu.aivle.heatguard_ai_back.controller;

import edu.aivle.heatguard_ai_back.dto.ApiResponse;
import edu.aivle.heatguard_ai_back.service.GuDongService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/regions")
public class GuDongController {
    private final GuDongService guDongService;

    //전체 조회
    @GetMapping
    public ApiResponse<Map<String, List<String>>> getAllGudong(){
        return ApiResponse.success(guDongService.getAllDongGu());
    }
}
