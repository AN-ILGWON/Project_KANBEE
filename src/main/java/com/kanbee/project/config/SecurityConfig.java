package com.kanbee.project.config;

import com.kanbee.project.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 스프링의 설정 클래스임을 나타냅니다.
@EnableWebSecurity // 스프링 시큐리티 설정을 활성화합니다.
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean // 비밀번호 암호화에 사용할 빈을 등록합니다. BCrypt 방식을 사용합니다.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // 시큐리티의 세부적인 보안 정책을 설정합니다.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        // H2 콘솔 사용을 위해 CSRF 보호를 제외합니다.
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                        .disable() // REST API 환경이나 테스트 편의를 위해 일단 비활성화합니다.
                )
                .headers(headers -> headers
                        // iframe 내에서 페이지가 렌더링될 수 있도록 설정 (H2 콘솔 등에서 필요)
                        .frameOptions(frame -> frame.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        // /admin/으로 시작하는 경로는 ADMIN 권한이 있어야 접근 가능합니다.
                        .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
                        // 마이페이지, 요청하기, 커뮤니티 글쓰기/댓글은 로그인(인증)이 필요합니다.
                        .requestMatchers(new AntPathRequestMatcher("/mypage/**"),
                                new AntPathRequestMatcher("/request/**"),
                                new AntPathRequestMatcher("/community/write"),
                                new AntPathRequestMatcher("/community/comment"))
                        .authenticated()
                        // 메인, 커뮤니티 목록, 로그인, 회원가입 및 정적 리소스는 누구나 접근 가능합니다.
                        .requestMatchers(new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/about"),
                                new AntPathRequestMatcher("/community"),
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/signup"),
                                new AntPathRequestMatcher("/css/**"),
                                new AntPathRequestMatcher("/js/**"),
                                new AntPathRequestMatcher("/images/**"),
                                new AntPathRequestMatcher("/uploads/**"))
                        .permitAll()
                        // 그 외의 모든 요청도 일단 허용합니다. (필요 시 denyAll()이나 authenticated()로 변경 가능)
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/login") // 커스텀 로그인 페이지 경로
                        .defaultSuccessUrl("/", true) // 로그인 성공 시 이동할 기본 경로
                        .failureUrl("/login?error=true") // 로그인 실패 시 이동할 경로
                        .permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 처리 URL
                        .logoutSuccessUrl("/") // 로그아웃 성공 후 이동할 경로
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID", "remember-me") // 쿠키 삭제 (자동 로그인 쿠키 포함)
                        .permitAll())
                .rememberMe(remember -> remember
                        .key("kanbee-secret-key") // 인증 토큰 생성용 키
                        .tokenValiditySeconds(86400 * 30) // 쿠키 유효기간 (30일)
                        .userDetailsService(userDetailsService) // 사용자 정보 조회 서비스 설정
                        .rememberMeParameter("remember-me") // 로그인 폼의 체크박스 name과 일치해야 함
                );

        return http.build();
    }
}
