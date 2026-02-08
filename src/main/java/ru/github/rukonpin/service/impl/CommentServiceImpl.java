package ru.github.rukonpin.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.github.rukonpin.dto.response.CommentResponse;
import ru.github.rukonpin.dto.request.CreateCommentForm;
import ru.github.rukonpin.exception.comment.CommentNotFoundException;
import ru.github.rukonpin.mapper.CreateCommentMapper;
import ru.github.rukonpin.model.Comment;
import ru.github.rukonpin.repository.CommentRepository;
import ru.github.rukonpin.service.CommentService;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<CommentResponse> getComments(Long id) {
        List<Comment> comments = commentRepository.findByPostId(id);
        return CreateCommentMapper.toResponseList(comments);
    }

    @Override
    public CommentResponse addCommentAndReturn(Long postId, CreateCommentForm form) {
        Comment comment = new Comment();
        comment.setContent(form.getContent());

        long commentId = commentRepository.insertComment(comment);
        int inserted = commentRepository.insertCommentToPost(postId, commentId);

        if (inserted == 0) {
            throw new CommentNotFoundException(postId);
        }

        Comment saved = commentRepository.findById(commentId);

        String createdAtStr = saved.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return new CommentResponse(
                saved.getId(),
                saved.getName(),
                saved.getContent(),
                saved.getImagePath(),
                createdAtStr
        );
    }

    @Override
    public void updateComment(Long id, CreateCommentForm form) {
        Comment comment = new Comment();
        comment.setContent(form.getContent());

        int updated = commentRepository.updateById(id, comment);

        if (updated == 0) {
            throw new CommentNotFoundException(id);
        }
    }

    @Override
    public long countComments(Long id) {
        return commentRepository.countById(id);
    }

    @Override
    public void deleteComment(Long id) {
        int deleted = commentRepository.deleteById(id);

        if (deleted == 0) {
            throw new CommentNotFoundException(id);
        }
    }
}
