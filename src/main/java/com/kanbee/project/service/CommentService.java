package com.kanbee.project.service;

import com.kanbee.project.mapper.CommentMapper;
import com.kanbee.project.model.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentMapper commentMapper;

    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentMapper.findByPostId(postId);
    }

    public void addComment(Comment comment) {
        commentMapper.insert(comment);
    }

    public Comment getCommentById(Long id) {
        return commentMapper.findById(id);
    }

    public void deleteComment(Long id) {
        commentMapper.delete(id);
    }
}
