package com.kanbee.project.model;

import java.time.LocalDateTime;

/**
 * 커뮤니티 게시글의 댓글 정보를 담는 모델 클래스입니다.
 */
public class Comment {
    private Long id;                // 댓글 고유 ID
    private Long postId;            // 소속된 게시글의 ID
    private String author;          // 댓글 작성자 닉네임 (실제로는 ID)
    private String nickname;        // 댓글 작성자 닉네임 (DB 조인)
    private String content;         // 댓글 본문 내용
    private LocalDateTime createdAt; // 작성 일시

    // 생성자
    public Comment() {}
    public Comment(Long postId, String author, String content) {
        this.postId = postId;
        this.author = author;
        this.content = content;
    }

    // Getter와 Setter 메소드들
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
