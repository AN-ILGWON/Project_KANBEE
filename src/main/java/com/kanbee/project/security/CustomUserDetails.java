package com.kanbee.project.security;

import com.kanbee.project.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * 스프링 시큐리티에서 인증된 사용자의 상세 정보를 다루는 클래스입니다.
 * DB의 User 모델을 시큐리티가 이해할 수 있는 UserDetails 형식으로 변환합니다.
 */
public class CustomUserDetails implements UserDetails {
    private final User user; // 실제 DB 사용자 정보 객체

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // 화면에 표시할 닉네임을 반환합니다.
    public String getNickname() {
        return user.getNickname();
    }

    // 사용자의 권한 정보를 반환합니다.
    public String getRole() {
        return user.getRole();
    }

    @Override // 사용자가 가진 권한(Role) 리스트를 반환합니다.
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // "ROLE_USER" 또는 "ROLE_ADMIN" 형태의 권한을 부여합니다.
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 아래 메서드들은 계정의 만료, 잠금, 비밀번호 만료, 활성화 여부를 체크합니다.
    // 현재는 모두 true를 반환하여 제한 없이 사용 가능하도록 설정했습니다.
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
