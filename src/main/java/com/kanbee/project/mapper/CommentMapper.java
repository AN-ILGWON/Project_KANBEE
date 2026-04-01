package com.kanbee.project.mapper;

import com.kanbee.project.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> findByPostId(@Param("postId") Long postId);

    void insert(Comment comment);

    Comment findById(@Param("id") Long id);

    void delete(@Param("id") Long id);
}
