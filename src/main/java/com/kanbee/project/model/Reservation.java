package com.kanbee.project.model;

import java.time.LocalDateTime;

/**
 * 사용자의 예약 요청 정보를 담는 모델 클래스입니다.
 */
public class Reservation {
    private Long id;                // 예약 고유 ID
    private String username;        // 예약 요청자 아이디
    private String category;        // 예약 카테고리 (RESTAURANT, POPUP, HAIR, SPA 등)
    private String requestTime;     // 예약 희망 일시
    private int headcount;          // 예약 인원
    private String requirements;    // 상세 요청 사항
    private String ticketLink;      // 참고할 티켓이나 정보 링크 (관리자 -> 사용자)
    private String referenceUrl;    // 사용자가 제공하는 참고 링크 (사용자 -> 관리자)
    private String imageUrl;        // 참고 이미지 경로 (캡쳐본 등)
    private String adminComment;    // 관리자 답변 또는 진행상황 메모
    private String status;          // 현재 상태 (PENDING, PROCESSING, COMPLETED, CANCELLED)
    private LocalDateTime createdAt; // 요청 작성 일시

    public Reservation() {}

    public Reservation(Long id, String username, String category, String requestTime, int headcount, String requirements, String ticketLink, String imageUrl, String adminComment, String status, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.category = category;
        this.requestTime = requestTime;
        this.headcount = headcount;
        this.requirements = requirements;
        this.ticketLink = ticketLink;
        this.imageUrl = imageUrl;
        this.adminComment = adminComment;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getter와 Setter 메소드들
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getRequestTime() { return requestTime; }
    public void setRequestTime(String requestTime) { this.requestTime = requestTime; }
    public int getHeadcount() { return headcount; }
    public void setHeadcount(int headcount) { this.headcount = headcount; }
    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    public String getTicketLink() { return ticketLink; }
    public void setTicketLink(String ticketLink) { this.ticketLink = ticketLink; }
    public String getReferenceUrl() { return referenceUrl; }
    public void setReferenceUrl(String referenceUrl) { this.referenceUrl = referenceUrl; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getAdminComment() { return adminComment; }
    public void setAdminComment(String adminComment) { this.adminComment = adminComment; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", category='" + category + '\'' +
                ", requestTime='" + requestTime + '\'' +
                ", headcount=" + headcount +
                ", requirements='" + requirements + '\'' +
                ", ticketLink='" + ticketLink + '\'' +
                ", referenceUrl='" + referenceUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", adminComment='" + adminComment + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
