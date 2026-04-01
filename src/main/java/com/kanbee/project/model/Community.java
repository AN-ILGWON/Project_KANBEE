package com.kanbee.project.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 커뮤니티 게시글 정보를 담는 모델 클래스입니다.
 */
public class Community {
    private Long id;                // 게시글 고유 ID
    private String author;          // 작성자 닉네임
    private String title;           // 게시글 제목
    private String content;         // 게시글 본문
    private String category;        // 카테고리 (TALK, TIP, QUESTION 등)
    private String imageUrl;        // 첨부 이미지 경로
    private LocalDateTime createdAt; // 작성 일시
    private String nickname;        // 작성자 닉네임 (DB 조인으로 가져옴)
    private List<Comment> comments; // 해당 게시글에 달린 댓글 리스트

    public Community() {}

    public Community(Long id, String author, String title, String content, String category, LocalDateTime createdAt) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdAt = createdAt;
    }

    // Getter와 Setter 메소드들
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
}
