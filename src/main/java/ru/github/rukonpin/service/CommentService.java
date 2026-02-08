package ru.github.rukonpin.service;

import ru.github.rukonpin.dto.response.CommentResponse;
import ru.github.rukonpin.dto.request.CreateCommentForm;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getComments(Long id);
    CommentResponse addCommentAndReturn(Long postId, CreateCommentForm form);
    void updateComment(Long id, CreateCommentForm form);
    long countComments(Long id);
    void deleteComment(Long id);
}
