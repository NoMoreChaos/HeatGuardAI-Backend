package edu.aivle.heatguard_ai_back.service;

import edu.aivle.heatguard_ai_back.dto.recommend.request.RecoLocRequest;
import edu.aivle.heatguard_ai_back.dto.recommend.response.RecoLocResponse;
import edu.aivle.heatguard_ai_back.dto.recommend.response.RecoLocResultDto;
import edu.aivle.heatguard_ai_back.entity.RecoLocEntity;
import edu.aivle.heatguard_ai_back.repository.RecoLocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecoLocService {

    private final RecoLocRepository recoLocRepository;

    @Transactional(readOnly = true)
    public RecoLocResponse recommend(RecoLocRequest request) {

        // 1) 기본 검증
        Integer targetCount = request.getTargetCount();
        Integer typeCd = request.getRecoLocTypeCd();

        if (targetCount == null || targetCount < 1 || targetCount > 5) {
            throw new IllegalArgumentException("target_count는 1~5만 가능합니다.");
        }
        if (typeCd == null || typeCd < 1 || typeCd > 3) {
            throw new IllegalArgumentException("reco_loc_type_cd는 1~3만 가능합니다.");
        }

        String gu = normalize(request.getTargetRegionGu());
        String dong = normalize(request.getTargetRegionDong());

        // 2) 조회
        PageRequest pageable = PageRequest.of(0, targetCount);

        List<RecoLocEntity> entities;
        if (gu == null) {
            entities = recoLocRepository.findAllByType(typeCd, pageable);
        } else if (dong == null) {
            entities = recoLocRepository.findAllByTypeAndGu(typeCd, gu, pageable);
        } else {
            entities = recoLocRepository.findAllByTypeAndGuAndDong(typeCd, gu, dong, pageable);
        }

        // 3) 응답 구성
        List<RecoLocResultDto> results = entities.stream()
                .map(e -> RecoLocResultDto.builder()
                        .lat(e.getGeeLoc().getGeeLocLat())
                        .lng(e.getGeeLoc().getGeeLocLng())
                        .recoLocRank(e.getRecoLocRank())
                        .geeLocAddress(e.getGeeLoc().getGeeAddressFull())
                        .recoLocRisk(e.getRecoLocRisk())
                        .recoLocDesc(e.getRecoLocDesc())
                        .build())
                .toList();

        String resultAddress = buildResultAddress(gu, dong);

        return RecoLocResponse.builder()
                .resultAddress(resultAddress)
                .resultCount(results.size()) // target_count와 다를 수 있음
                .result(results)
                .build();
    }

    private String normalize(String s) {
        if (s == null) return null;
        String trimmed = s.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String buildResultAddress(String gu, String dong) {
        // 요구사항 그대로:
        // - gu/dong null => "서울시 전체"
        // - dong null => "서울시 {gu}"
        // - 둘다 있으면 => "서울시 {gu} {dong}"
        if (gu == null) return "서울특별시";
        if (dong == null) return "서울특별시 " + gu;
        return "서울특별시 " + gu + " " + dong;
    }
}
