package com.kanbee.project.model;

import java.time.LocalDateTime;

/**
 * 사용자 정보를 담는 도메인 모델 클래스입니다.
 */
public class User {
    private Long id;                // 고유 식별자
    private String username;        // 로그인 아이디
    private String password;        // 암호화된 비밀번호
    private String nickname;        // 서비스 내에서 사용할 닉네임
    private String role;            // 권한 ('ADMIN' 또는 'USER')
    private LocalDateTime createdAt; // 계정 생성 일시

    public User() {}

    public User(Long id, String username, String password, String nickname, String role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getter와 Setter 메소드들
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
