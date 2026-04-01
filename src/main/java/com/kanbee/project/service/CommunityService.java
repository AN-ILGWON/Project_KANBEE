package com.kanbee.project.service;

import com.kanbee.project.mapper.CommentMapper;
import com.kanbee.project.mapper.CommunityMapper;
import com.kanbee.project.model.Community;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 커뮤니티 게시판의 비즈니스 로직을 담당하는 서비스 계층입니다.
public class CommunityService {
    // 게시글(Community)과 댓글(Comment) 처리를 위해 각각의 Mapper를 주입받습니다.
    private final CommunityMapper communityMapper;
    private final CommentMapper commentMapper;

    public CommunityService(CommunityMapper communityMapper, CommentMapper commentMapper) {
        this.communityMapper = communityMapper;
        this.commentMapper = commentMapper;
    }

    /**
     * 모든 게시글 목록을 가져옵니다. 
     * 이때 각 게시글에 달린 댓글들도 함께 불러와서 세팅해줍니다.
     * @return 댓글이 포함된 전체 게시글 리스트
     */
    public List<Community> getAllPosts() {
        List<Community> posts = communityMapper.findAll(); // 1. 모든 게시글 조회
        for (Community post : posts) {
            // 2. 각 게시글 ID를 기준으로 해당 글의 댓글들을 조회하여 포함시킵니다.
            post.setComments(commentMapper.findByPostId(post.getId()));
        }
        return posts;
    }

    /**
     * 제목이나 내용에 키워드가 포함된 게시글을 검색합니다.
     * @param keyword 검색어
     * @return 검색된 게시글 리스트
     */
    public List<Community> searchPosts(String keyword) {
        List<Community> posts = communityMapper.search(keyword); // 1. 검색어로 게시글 조회
        for (Community post : posts) {
            // 2. 검색된 각 게시글의 댓글들을 함께 가져옵니다.
            post.setComments(commentMapper.findByPostId(post.getId()));
        }
        return posts;
    }  

    /**
     * 새 게시글을 DB에 저장합니다.
     * @param community 게시글 데이터
     */
    public void savePost(Community community) {
        communityMapper.insert(community);
    }

    /**
     * 기존 게시글 내용을 수정합니다.
     * @param community 수정할 데이터가 담긴 객체
     */
    public void updatePost(Community community) {
        communityMapper.update(community);
    }

    /**
     * 게시글을 삭제합니다.
     * @param id 삭제할 게시글 번호
     */
    public void deletePost(Long id) {
        communityMapper.delete(id);
    }

    /**
     * 특정 작성자의 게시글을 모두 삭제합니다.
     * @param author 작성자 아이디
     */
    public void deletePostsByAuthor(String author) {
        communityMapper.deleteByAuthor(author);
    }

    /**
     * 게시글 ID로 특정 게시글 하나를 조회합니다.
     * @param id 게시글 번호
     * @return 게시글 상세 데이터
     */
    public Community getPostById(Long id) {
        Community post = communityMapper.findById(id);
        if (post != null) {
            post.setComments(commentMapper.findByPostId(id));
        }
        return post;
    }

    /**
     * 특정 작성자(닉네임 기준)가 쓴 게시글들만 모아서 가져옵니다.
     * @param author 작성자 닉네임
     * @return 해당 작성자의 게시글 리스트
     */
    public List<Community> getPostsByAuthor(String author) {
        List<Community> posts = communityMapper.findByAuthor(author); // 1. 작성자 이름으로 게시글 조회
        for (Community post : posts) {
            // 2. 각 게시글의 댓글들도 세팅합니다.
            post.setComments(commentMapper.findByPostId(post.getId()));
        }
        return posts;
    }

    // 모든 게시글을 삭제합니다. (데이터 초기화용)
    public void deleteAll() {
        communityMapper.deleteAll();
    }
}
