package com.kanbee.project.service;

import com.kanbee.project.mapper.ReservationMapper;
import com.kanbee.project.model.Reservation;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // 예약 관련 비즈니스 로직을 처리하는 서비스 계층입니다.
public class ReservationService {
    // DB와 소통하는 Mapper와 활동 기록을 남기는 LoggingService를 주입받습니다.
    private final ReservationMapper reservationMapper;
    private final KanbeeLoggingService loggingService;

    public ReservationService(ReservationMapper reservationMapper, KanbeeLoggingService loggingService) {
        this.reservationMapper = reservationMapper;
        this.loggingService = loggingService;
    }

    // 관리자 페이지용: 시스템에 등록된 모든 예약 목록을 가져옵니다.
    public List<Reservation> getAllReservations() {
        return reservationMapper.findAll();
    }

    // 사용자가 새로운 예약을 신청할 때 실행됩니다.
    public void requestReservation(Reservation reservation) {
        reservationMapper.insert(reservation); // DB에 예약 정보 저장
        try {
            // 활동 로그에 예약 신청 사실을 기록합니다.
            loggingService.logActivity("New Reservation Request: " + reservation.getCategory());
        } catch (Exception e) {
            // 로그 기록 중 에러가 발생해도 예약 자체는 성공해야 하므로 에러를 무시합니다.
        }
    }

    // (관리자) 예약의 상태(대기, 진행중, 완료 등)를 변경합니다.
    public void updateStatus(Long id, String status) {
        reservationMapper.updateStatus(id, status); // DB의 상태 값 수정
        try {
            loggingService.logActivity("Status Updated (ID: " + id + "): " + status);
        } catch (Exception e) {}
    }

    // (관리자) 예약 건에 대해 관리자가 코멘트(답변)를 남깁니다.
    public void updateAdminComment(Long id, String comment) {
        reservationMapper.updateAdminComment(id, comment); // DB의 코멘트 컬럼 수정
        try {
            loggingService.logActivity("Admin Comment Added (ID: " + id + "): " + comment);
        } catch (Exception e) {}
    }

    // (관리자) 예약 완료 후 티켓 링크를 등록합니다.
    public void updateTicketLink(Long id, String ticketLink) {
        reservationMapper.updateTicketLink(id, ticketLink); // DB의 티켓 링크 컬럼 수정
        try {
            loggingService.logActivity("Ticket Link Updated (ID: " + id + "): " + ticketLink);
        } catch (Exception e) {}
    }

    // 특정 사용자(아이디 기준)의 예약 내역만 필터링하여 가져옵니다.
    public List<Reservation> getReservationsByUsername(String username) {
        return reservationMapper.findByUsername(username);
    }

    public Reservation getReservationById(Long id) {
        return reservationMapper.findById(id);
    }

    // 모든 예약을 삭제합니다. (데이터 초기화용)
    public void deleteAll() {
        reservationMapper.deleteAll();
    }

    // (관리자) 예약 상세 정보를 수정합니다.
    public void updateReservation(Reservation reservation) {
        reservationMapper.updateReservation(reservation);
        try {
            loggingService.logActivity("Reservation Updated (ID: " + reservation.getId() + ")");
        } catch (Exception e) {}
    }
}
