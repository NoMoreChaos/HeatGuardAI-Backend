package edu.aivle.heatguard_ai_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai-loc")
public class RecommendLocationController {

    // AI 최적 위치 추천 리스트
    @PostMapping
    public String postRecommendLocation() {
        return "recommendLocation ok";
    }
}
