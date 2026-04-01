package com.kanbee.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration // 웹 관련 설정을 담당하는 클래스입니다.
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.path}") // application.properties에 설정된 파일 업로드 실제 경로
    private String uploadPath;

    @Value("${uploadPath}") // 웹에서 접근할 가상 업로드 경로 (예: /uploads)
    private String resourcePath;

    @Bean // 다국어 지원을 위한 언어 설정(Locale) 관리 빈입니다.
    public LocaleResolver localeResolver() {
        CookieLocaleResolver clr = new CookieLocaleResolver();
        clr.setDefaultLocale(Locale.JAPANESE); // 프로젝트의 기본 언어를 일본어로 설정합니다.
        clr.setCookieName("lang"); // 쿠키 이름을 lang으로 설정합니다.
        clr.setCookieMaxAge(60 * 60 * 24 * 365); // 쿠키 유효기간을 1년으로 설정합니다.
        return clr;
    }

    @Bean // URL 파라미터를 통해 언어를 변경할 수 있게 해주는 인터셉터입니다. (예: ?lang=ko)
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang"); // 언어 변경을 위해 사용할 파라미터 키값입니다.
        return lci;
    }

    @Override // 인터셉터를 스프링 MVC 설정에 등록합니다.
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Override // 실제 서버의 파일 경로를 웹 URL과 매핑합니다.
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // resourcePath(/uploads/**)로 들어오는 요청을 실제 uploadPath(C:/kanbee/uploads/) 경로와 연결합니다.
        registry.addResourceHandler(resourcePath + "/**")
                .addResourceLocations("file:///" + uploadPath + "/");
    }
}
