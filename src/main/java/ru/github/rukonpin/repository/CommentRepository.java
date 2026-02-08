package ru.github.rukonpin.repository;

import ru.github.rukonpin.model.Comment;

import java.util.List;

public interface CommentRepository {
    List<Comment> findByPostId(Long id);
    Comment findById(Long id);
    long insertComment(Comment comment);
    int insertCommentToPost(long postId, long commentId);
    int updateById(Long id, Comment comment);
    long countById(Long id);
    int deleteById(Long id);
}
