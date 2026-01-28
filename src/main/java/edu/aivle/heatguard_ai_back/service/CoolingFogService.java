package edu.aivle.heatguard_ai_back.service;

import edu.aivle.heatguard_ai_back.dto.coolingfog.response.CoolingFogDetailResponse;
import edu.aivle.heatguard_ai_back.dto.coolingfog.response.CoolingFogListResponse;
import edu.aivle.heatguard_ai_back.entity.CoolingFogEntity;
import edu.aivle.heatguard_ai_back.entity.CoolingFogMeasureEntity;
import edu.aivle.heatguard_ai_back.repository.CoolingFogMeasureRepository;
import edu.aivle.heatguard_ai_back.repository.CoolingFogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CoolingFogService {
    // 필드 주입
    private final CoolingFogRepository coolingFogRepository;
    private final CoolingFogMeasureRepository coolingFogMeasureRepository;

    // detail에서 시간별 리스트 노출 개수 위산 계산 메서드
    private String resolveHourKey(int nowHour) {
        if(nowHour < 6) return "00";
        if(nowHour < 12) return "06";
        if(nowHour < 18) return "12";
        return "18";
    }

    public CoolingFogListResponse getCoolingFogList() {
        List<CoolingFogEntity> entities = coolingFogRepository.findAll();
        List<CoolingFogListResponse.CoolingFog> list = entities.stream()
                .map(e -> new CoolingFogListResponse.CoolingFog(
                        e.getCf_cd(),
                        e.getCf_lat(),
                        e.getCf_lng()
                ))
                .toList();

        return new CoolingFogListResponse(list.size(), list);
    }

    private Double itemTemp(Double value) {
        if (value == null) return null;
        return Math.round(value * 10.0) / 10.0;
    }

    public CoolingFogDetailResponse getCoolingFogDetail(String cf_cd) {
        CoolingFogEntity entity = coolingFogRepository.findById(cf_cd)
                .orElseThrow(() -> new IllegalArgumentException("쿨링포그 상세 데이터가 없습니다."));
        int nowHour = LocalTime.now().getHour();
        String hourKey = resolveHourKey(nowHour);

        // CF_MEASURE_TB에서 현재시간 이하 데이터 조회
        List<CoolingFogMeasureEntity> measures = coolingFogMeasureRepository.findMeasures(cf_cd, hourKey);

        // time Map + 온습도
        Map<String, CoolingFogDetailResponse.TimeMeasure> timeMap = new LinkedHashMap<>();
        Double selected = null;
        Double nearby = null;
        Double hum = null;

        for (CoolingFogMeasureEntity m : measures) {
            String key = m.getCf_measure_hour() + ":00";
            Double coolingTemp = itemTemp(m.getCf_measure_selected_temp() * 0.9);
            timeMap.put(key, new CoolingFogDetailResponse.TimeMeasure(
                    coolingTemp,
                    m.getCf_measure_nearby_temp(),
                    m.getCf_measure_hum_per()
            ));

            // 현재시각 이하 최신값
            selected = coolingTemp;
            nearby = m.getCf_measure_nearby_temp();
            hum = m.getCf_measure_hum_per();
        }


        return new CoolingFogDetailResponse (
                entity.getCf_city_gu(),
                entity.getCf_city_dong(),
                entity.getCf_location(),
                entity.getCf_address(),
                entity.getCf_lat(),
                entity.getCf_lng(),
                entity.isCf_state(),

                // cf_measure_tb 데이터
                selected,
                nearby,
                hum,

                entity.getCf_inst_date(),
                entity.getCf_manage_dept(),
                entity.getCf_manager_nm(),
                entity.getCf_manager_contact(),

                // time
                timeMap
        );
    }
}
