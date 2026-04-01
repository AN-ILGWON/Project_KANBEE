package com.kanbee.project.mapper;

import com.kanbee.project.model.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper // 예약 관련 DB 연동을 위한 MyBatis 매퍼 인터페이스입니다.
public interface ReservationMapper {
    /**
     * 시스템의 모든 예약 목록을 가져옵니다. (관리자용)
     * @return 모든 예약 리스트
     */
    List<Reservation> findAll();

    /**
     * 새로운 예약 신청 정보를 저장합니다.
     * @param reservation 예약 데이터 객체
     */
    void insert(Reservation reservation);

    /**
     * 특정 예약의 상태를 변경합니다. (예: 대기 -> 진행중 -> 완료)
     * @param id 예약 번호
     * @param status 변경할 상태 값
     */
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 관리자가 예약 건에 대해 답변(코멘트)을 작성/수정합니다.
     * @param id 예약 번호
     * @param adminComment 관리자 답변 내용
     */
    void updateAdminComment(@Param("id") Long id, @Param("adminComment") String adminComment);

    /**
     * 예약 완료 시 발권된 티켓 링크를 등록/수정합니다.
     * @param id 예약 번호
     * @param ticketLink 티켓 링크 URL
     */
    void updateTicketLink(@Param("id") Long id, @Param("ticketLink") String ticketLink);

    /**
     * 특정 예약의 상세 정보를 조회합니다.
     * @param id 예약 번호
     * @return 예약 정보 객체
     */
    Reservation findById(@Param("id") Long id);

    /**
     * 특정 사용자(ID 기준)가 신청한 모든 예약 목록을 가져옵니다. (사용자용)
     * @param username 사용자 아이디
     * @return 해당 사용자의 예약 리스트
     */
    List<Reservation> findByUsername(@Param("username") String username);

    /**
     * 모든 예약을 삭제합니다. (초기화용)
     */
    void deleteAll();

    /**
     * 예약 상세 정보를 수정합니다. (관리자용)
     * @param reservation 수정할 예약 정보
     */
    void updateReservation(Reservation reservation);
}
