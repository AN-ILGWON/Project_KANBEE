package com.kanbee.project.service;

import org.springframework.stereotype.Service;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 서비스 내부의 주요 활동 내용을 파일 로그로 기록하는 서비스입니다.
 */
@Service
public class KanbeeLoggingService {
    // 로그가 저장될 파일명
    private static final String LOG_FILE = "kanbee_activity.log";

    /**
     * 활동 내용을 타임스탬프와 함께 로그 파일에 저장합니다.
     * @param activity 기록할 활동 내용
     */
    public void logActivity(String activity) throws IOException {
        // 파일을 추가(append) 모드로 열어 기록합니다.
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            // 현재 시간을 "yyyy-MM-dd HH:mm:ss" 형식으로 포맷팅
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // 로그 형식: [시간] [ACTIVITY] 활동내용
            out.println("[" + timestamp + "] [ACTIVITY] " + activity);
        }
    }
}
