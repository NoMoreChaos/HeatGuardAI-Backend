package edu.aivle.heatguard_ai_back.controller;

import edu.aivle.heatguard_ai_back.dto.ApiResponse;
import edu.aivle.heatguard_ai_back.dto.recommend.request.RecoLocRequest;
import edu.aivle.heatguard_ai_back.dto.recommend.response.RecoLocResponse;
import edu.aivle.heatguard_ai_back.service.RecoLocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecoLocController {

    private final RecoLocService recoLocService;

    // 쿨링포그 추천 위치 반환
    @PostMapping("/ai-loc")
    public ResponseEntity<ApiResponse<RecoLocResponse>> recommend(@RequestBody RecoLocRequest request) {
        try {
            RecoLocResponse response = recoLocService.recommend(request);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("요청 처리 중 오류가 발생했습니다."));
        }
    }
}
