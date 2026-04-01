package com.kanbee.project.service;

import com.kanbee.project.mapper.HotPlaceMapper;
import com.kanbee.project.model.HotPlace;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class HotPlaceService {

    private final HotPlaceMapper hotPlaceMapper;
    private final AiRecommendationService aiRecommendationService;

    public HotPlaceService(HotPlaceMapper hotPlaceMapper, AiRecommendationService aiRecommendationService) {
        this.hotPlaceMapper = hotPlaceMapper;
        this.aiRecommendationService = aiRecommendationService;
    }

    public List<HotPlace> getAllHotPlaces() {
        return hotPlaceMapper.findAll();
    }

    public HotPlace getHotPlaceById(Long id) {
        return hotPlaceMapper.findById(id);
    }

    public void saveHotPlace(HotPlace hotPlace) {
        hotPlaceMapper.insert(hotPlace);
    }

    /**
     * AI 기반 추천 장소 목록을 가져옵니다.
     * 1. 먼저 실제 OpenAI API를 통해 추천을 시도합니다.
     * 2. API 키가 없거나 실패할 경우, 기존 Rule-based 로직(DB 검색)으로 폴백합니다.
     * 
     * @param reservationCategory 예약 카테고리 (RESTAURANT, CAFE, etc.)
     * @return 추천 장소 리스트
     */
    public List<HotPlace> getAiRecommendations(String reservationCategory) {
        String searchCategory;
        
        // 예약 카테고리를 사람이 읽기 좋은 형태로 변환
        if (reservationCategory == null) {
            searchCategory = "Restaurant"; 
        } else if (reservationCategory.equals("RESTAURANT")) {
            searchCategory = "Restaurant (Korean Food)";
        } else if (reservationCategory.equals("CAFE")) {
            searchCategory = "Cafe / Dessert";
        } else if (reservationCategory.equals("POPUP")) {
            searchCategory = "Pop-up Store / Shopping";
        } else if (reservationCategory.equals("HAIR") || reservationCategory.equals("BEAUTY")) {
            searchCategory = "K-Beauty / Hair Salon";
        } else {
            searchCategory = "Tourist Attraction";
        }
        
        // 1. OpenAI API 호출 시도 부분 제거 (KanbeeController에서 직접 호출)
        /*
        try {
            List<HotPlace> aiResults = aiRecommendationService.getRecommendations(searchCategory);
            if (!aiResults.isEmpty()) {
                return aiResults;
            }
        } catch (Exception e) {
            // API 호출 실패 시 무시하고 폴백 로직 진행
            System.err.println("AI Service failed, falling back to DB: " + e.getMessage());
        }
        */

        // 2. 폴백: 기존 Rule-based DB 검색 로직
        String dbCategory;
        if (reservationCategory == null) {
            dbCategory = "RESTAURANT";
        } else if (reservationCategory.equals("RESTAURANT")) {
            dbCategory = "맛집";
        } else if (reservationCategory.equals("CAFE")) {
            dbCategory = "카페";
        } else if (reservationCategory.equals("POPUP")) {
            dbCategory = "팝업스토어";
        } else if (reservationCategory.equals("HAIR") || reservationCategory.equals("BEAUTY")) {
            dbCategory = "뷰티";
        } else {
            dbCategory = "명소";
        }
        
        List<HotPlace> recommendations = hotPlaceMapper.findByCategory(dbCategory);
        
        if (recommendations == null || recommendations.isEmpty()) {
            recommendations = hotPlaceMapper.findAll();
            if (recommendations.size() > 3) {
                Collections.shuffle(recommendations);
                return recommendations.subList(0, 3);
            }
        } else {
            if (recommendations.size() > 3) {
                Collections.shuffle(recommendations);
                return recommendations.subList(0, 3);
            }
        }
        
        return recommendations != null ? recommendations : Collections.emptyList();
    }
}
