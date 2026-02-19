package edu.aivle.heatguard_ai_back.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

@Slf4j
@Component
public class GuDongJsonLoader {

    private final ObjectMapper objectMapper;

    @Getter
    private Map<String, List<String>> guDongMap = new HashMap<>();

    public GuDongJsonLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void load() {
        try (InputStream is = new ClassPathResource("gu_dong_legal.json").getInputStream()) {
            Map<String, List<String>> data =
                    objectMapper.readValue(is, new TypeReference<Map<String, List<String>>>() {
                    });
            guDongMap = Collections.unmodifiableMap(data);

//            log.info("구 개수 :{}", guDongMap.size());
//            log.info("구 목록 5개:{}", guDongMap.keySet().stream().limit(5).toList());
//            log.info("성북구 목록:{}", guDongMap.get("성북구"));

        } catch (Exception e) {
            throw new IllegalStateException("gu_dong_legal.json 로딩 실패", e);
        }
    }

    public List<String> getGuList() {
        return new ArrayList<>(guDongMap.keySet());
    }

    public List<String> getDongList(String gu) {
        return guDongMap.getOrDefault(gu, List.of());
    }

    public boolean hasDong(String gu, String dong) {
        return guDongMap.getOrDefault(gu, List.of()).contains(dong);
    }
}