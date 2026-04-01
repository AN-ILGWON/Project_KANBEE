package com.kanbee.project.controller;

import com.kanbee.project.model.HotPlace;
import com.kanbee.project.service.HotPlaceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
public class AiTestController {

    private final HotPlaceService hotPlaceService;
    private final com.kanbee.project.service.AiRecommendationService aiRecommendationService;

    public AiTestController(HotPlaceService hotPlaceService, com.kanbee.project.service.AiRecommendationService aiRecommendationService) {
        this.hotPlaceService = hotPlaceService;
        this.aiRecommendationService = aiRecommendationService;
    }

    @GetMapping("/test/ai-recommendation")
    public Map<String, Object> testAiRecommendation(@RequestParam(value = "category", defaultValue = "RESTAURANT") String category) {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 입력받은 카테고리 확인
        result.put("input_category", category);
        
        // 2. AI 추천 로직 실행
        List<HotPlace> recommendations = aiRecommendationService.getRecommendations(category);
        
        // 3. 결과 반환
        result.put("recommendation_count", recommendations.size());
        result.put("recommendations", recommendations);
        
        // 4. 메시지
        if (recommendations.isEmpty()) {
            result.put("message", "AI 응답 없음 (Fallback 필요)");
        } else {
            result.put("message", "AI 추천 성공! (Gemini API)");
        }
        
        return result;
    }
}
