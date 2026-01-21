package edu.aivle.heatguard_ai_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cf")
public class CoolingFogController {

    // 쿨링포그 전체 리스트
    @GetMapping("/list")
    public String getCoolingFogList() {
        return "coolingfog list ok";
    }

    // 쿨링포그 상세
    @GetMapping("/{cf_cd}")
    public String getCoolingFogDetail(@PathVariable String cf_cd) {
        return cf_cd + " detail ok";
    }
}
