package com.kanbee.project.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis 설정 클래스입니다.
 * DB 컬럼명(snake_case)과 자바 필드명(camelCase)을 자동으로 매핑해주는 설정을 포함합니다.
 */
@Configuration
public class MyBatisConfig {

    @Bean // MyBatis 설정을 커스터마이징합니다.
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return (org.apache.ibatis.session.Configuration configuration) -> {
            // true 설정 시 user_name 컬럼을 userName 필드에 자동으로 담아줍니다.
            configuration.setMapUnderscoreToCamelCase(true);
        };
    }
}
