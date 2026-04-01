package com.kanbee.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 스프링 부트 애플리케이션의 핵심 설정을 자동화하는 어노테이션입니다. (컴포넌트 스캔, 자동 설정 등을 포함)
@MapperScan("com.kanbee.project.mapper") // MyBatis 매퍼 인터페이스들을 스캔하여 스프링 빈으로 등록할 패키지 경로를 지정합니다.
public class KanbeeApplication {
    // 애플리케이션의 진입점(Entry Point)입니다.
    public static void main(String[] args) {
        // 스프링 애플리케이션을 구동하고 내장 톰캣 서버를 실행합니다.
        SpringApplication.run(KanbeeApplication.class, args);
    }
}
