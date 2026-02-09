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

import java.util.Arrays;
import java.util.Collections;
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

        if (targetCount == null || targetCount < 1 || targetCount > 7) {
            throw new IllegalArgumentException("target_count는 1~7만 가능합니다.");
        }
        if (typeCd == null || typeCd < 0 || typeCd > 2) {
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
                        // GEE_LOC_TB
                        .lat(e.getGeeLoc().getGeeLocLat())
                        .lng(e.getGeeLoc().getGeeLocLng())
                        .geeAddressFull(e.getGeeLoc().getGeeAddressFull())

                        // RECO_LOC_TB
                        .recoLocRank(e.getRecoLocRank())
                        .recoLocPopuLevel(e.getRecoLocPopuLevel())
                        .recoLocVulnerableLevel(e.getRecoLocVulnerableLevel())
                        .recoLocFeelTemp(e.getRecoLocFeelTemp())
                        .recoLocLstLevel(e.getRecoLocLstLevel())
                        .recoLocNdviLevel(e.getRecoLocNdiviLevel())
                        .recoLocTotalScore(e.getRecoLocTotalScore())
                        .recoLocDesc(toDescList(e.getRecoLocDesc()))
                        .build())
                .toList();

        String resultAddress = buildResultAddress(gu, dong);

        return RecoLocResponse.builder()
                .resultAddress(resultAddress)
                .resultCount(results.size()) // target_count와 다를 수 있음
                .result(results)
                .build();
    }

    /**
     * RECO_LOC_DESC(TEXT)를 API의 List<String> 형태로 변환
     */
    private List<String> toDescList(String descText) {
        if (descText == null) return Collections.emptyList();

        String trimmed = descText.trim();
        if (trimmed.isEmpty()) return Collections.emptyList();

        return Arrays.stream(trimmed.split("\\r?\\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private String normalize(String s) {
        if (s == null) return null;
        String trimmed = s.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String buildResultAddress(String gu, String dong) {
        // - gu/dong null => "서울시 전체"
        // - dong null => "서울시 {gu}"
        // - 둘다 있으면 => "서울시 {gu} {dong}"
        if (gu == null) return "서울특별시";
        if (dong == null) return "서울특별시 " + gu;
        return "서울특별시 " + gu + " " + dong;
    }
}
