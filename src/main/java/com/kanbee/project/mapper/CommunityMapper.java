package com.kanbee.project.mapper;

import com.kanbee.project.model.Community;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper // MyBatis 매퍼 인터페이스임을 나타내며, XML의 SQL과 연결됩니다.
public interface CommunityMapper {
    /**
     * 모든 게시글 목록을 최신순으로 가져옵니다.
     * @return 게시글 리스트
     */
    List<Community> findAll();

    /**
     * 키워드를 사용하여 게시글을 검색합니다. (제목 또는 내용)
     * @param keyword 검색어
     * @return 검색된 게시글 리스트
     */
    List<Community> search(@Param("keyword") String keyword);

    /**
     * 게시글 ID를 사용하여 특정 게시글 정보를 가져옵니다.
     * @param id 게시글 번호
     * @return 게시글 객체
     */
    Community findById(@Param("id") Long id);

    /**
     * 특정 작성자가 쓴 게시글 목록을 가져옵니다.
     * @param author 작성자 닉네임
     * @return 해당 작성자의 게시글 리스트
     */
    List<Community> findByAuthor(@Param("author") String author);

    /**
     * 새로운 게시글을 등록합니다.
     * @param community 등록할 게시글 데이터
     */
    void insert(Community community);

    /**
     * 게시글 정보를 수정합니다.
     * @param community 수정할 게시글 데이터
     */
    void update(Community community);

    /**
     * 특정 게시글을 삭제합니다.
     * @param id 삭제할 게시글 번호
     */
    void delete(@Param("id") Long id);

    /**
     * 특정 작성자의 모든 게시글을 삭제합니다.
     * @param author 작성자 닉네임(아이디)
     */
    void deleteByAuthor(@Param("author") String author);

    /**
     * 모든 게시글을 삭제합니다. (초기화용)
     */
    void deleteAll();
}
