package com.kanbee.project.service;

import com.kanbee.project.mapper.UserMapper;
import com.kanbee.project.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // 이 클래스가 스프링의 서비스 레이어임을 나타냅니다. 비즈니스 로직을 처리하는 핵심 계층입니다.
public class UserService {
    // DB와 통신하는 Mapper 인터페이스와 비밀번호 암호화를 위한 PasswordEncoder를 주입받습니다.
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 새로운 사용자를 등록(회원가입)합니다.
     * @param user 회원 정보 데이터 객체
     * @return 가입 성공 여부
     */
    @Transactional // 메서드 실행 중 하나라도 실패하면 모든 작업을 취소(Rollback)하여 데이터의 일관성을 유지합니다.
    public boolean register(User user) {
        // 1. 이미 존재하는 아이디인지 확인합니다.
        if (userMapper.checkUsername(user.getUsername()) > 0) {
            return false; // 중복된 아이디면 가입 실패 처리
        }
        
        // 2. 권한이 설정되지 않은 경우 기본 권한인 'USER'를 부여합니다.
        if (user.getRole() == null) {
            user.setRole("USER");
        }
        
        // 3. 보안을 위해 비밀번호를 그대로 저장하지 않고 암호화하여 저장합니다.
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 4. 암호화된 비밀번호를 포함한 사용자 정보를 DB에 저장합니다.
        userMapper.register(user);
        return true; // 가입 성공
    }

    /**
     * 아이디로 사용자 정보를 조회합니다.
     * @param username 사용자 아이디
     * @return 사용자 객체
     */
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    /**
     * 아이디 중복 여부를 확인합니다.
     * @param username 확인할 아이디
     * @return 사용 가능 여부 (존재하지 않으면 true)
     */
    public boolean isUsernameAvailable(String username) {
        if (username == null) {
            return false;
        }
        String trimmedUsername = username.trim();
        
        // Prevent critical accounts from being checked as available (Safety Net)
        if ("superadmin".equalsIgnoreCase(trimmedUsername) || "admin".equalsIgnoreCase(trimmedUsername)) {
            return false;
        }
        
        return userMapper.checkUsername(trimmedUsername) == 0;
    }

    /**
     * 닉네임 중복 여부를 확인합니다.
     * @param nickname 확인할 닉네임
     * @return 사용 가능 여부 (존재하지 않으면 true)
     */
    public boolean isNicknameAvailable(String nickname) {
        return userMapper.checkNickname(nickname) == 0;
    }

    /**
     * 사용자 비밀번호 변경
     * @param username 사용자 아이디
     * @param password 새로운 비밀번호
     */
    @Transactional
    public void updatePassword(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        userMapper.updatePassword(username, encodedPassword);
    }

    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}
