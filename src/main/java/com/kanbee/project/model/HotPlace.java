package com.kanbee.project.model;

/**
 * 추천 핫플레이스 정보를 담는 모델 클래스입니다.
 */
public class HotPlace {
    private Long id;                // 장소 고유 ID
    private String name;            // 장소 이름
    private String category;        // 카테고리 (카페, 식당, 랜드마크 등)
    private String location;        // 지역/위치
    private String description;     // 상세 설명
    private String imageUrl;        // 이미지 경로 또는 URL
    private String tag;             // 해시태그 (예: #맛집 #분위기)

    public HotPlace() {}

    public HotPlace(Long id, String name, String category, String location, String description, String imageUrl, String tag) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.location = location;
        this.description = description;
        this.imageUrl = imageUrl;
        this.tag = tag;
    }

    // Getter와 Setter 메소드들
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
}
