package edu.aivle.heatguard_ai_back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class GuDongService {
    private final GuDongJsonLoader guDongJsonLoader;

    // 전체 구/동 맵 반환
    public Map<String, List<String>> getAllDongGu(){

        //구(key)를 오름차순으로 정렬
        Map<String, List<String>> sorted = new TreeMap<>();

        // 구+동 하나(onegu) 꺼내서 (구정렬 -> 동정렬) 반복
        for (Map.Entry<String, List<String>> onegu
                : guDongJsonLoader.getGuDongMap().entrySet()){

            //동 목록 복사 후 정렬
            List<String> dongs = new ArrayList<>(onegu.getValue());
            Collections.sort(dongs);

            //구 key값에 정렬된 동 리스트 넣기
            //동 수정 못하고 MAP잠금처리 : unmodifiableList
            sorted.put(onegu.getKey(), Collections.unmodifiableList(dongs));
        }
        // 수정못하게 MAP잠금처리 : unmodifiableMap
        return Collections.unmodifiableMap(sorted);
    }
}
