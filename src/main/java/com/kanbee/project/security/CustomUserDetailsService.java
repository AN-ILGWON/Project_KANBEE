package com.kanbee.project.security;

import com.kanbee.project.mapper.UserMapper;
import com.kanbee.project.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 스프링 시큐리티에서 사용자 정보를 DB로부터 로드하는 역할을 담당하는 서비스입니다.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper; // DB 접근을 위한 매퍼

    public CustomUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override // 사용자가 입력한 username(아이디)을 통해 DB에서 사용자 정보를 찾아 UserDetails로 반환합니다.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            // 사용자를 찾지 못한 경우 시큐리티 예외를 발생시킵니다.
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // 찾은 사용자 정보를 CustomUserDetails 객체로 감싸서 반환합니다.
        return new CustomUserDetails(user);
    }
}
