package ru.github.rukonpin.repository.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.github.rukonpin.config.DatabaseConfig;
import ru.github.rukonpin.model.Comment;
import ru.github.rukonpin.repository.CommentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        CommentRepositoryImpl.class,
        DatabaseConfig.class
})
@Transactional
class CommentRepositoryImplIT {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void countById_shouldReturnCommentCount_whenPostExists() {
        Long postId = 2L;

        long result = commentRepository.countById(postId);

        assertEquals(2, result);
    }

    @Test
    void insertComment_shouldReturnGeneratedId_whenCommentCreated() {
        Comment newComment = new Comment();
        newComment.setContent("New Content");

        long generatedId = commentRepository.insertComment(newComment);

        assertTrue(generatedId > 0);

        Comment savedComment = commentRepository.findById(generatedId);

        assertNotNull(savedComment);
        assertEquals("New Content", savedComment.getContent());
    }

    @Test
    void updateById_shouldUpdateSuccessfully_whenCommentExists() {
        Long commentId = 1L;
        Comment beforeComment = commentRepository.findById(commentId);

        Comment updateComment = new Comment();
        updateComment.setContent("Мне не понравилось");

        int result = commentRepository.updateById(commentId, updateComment);

        assertEquals(1, result);

        Comment afterComment = commentRepository.findById(commentId);
        assertEquals("Мне не понравилось", afterComment.getContent());
        assertNotEquals(beforeComment.getContent(), afterComment.getContent());
    }

    @Test
    void findByPostId_shouldReturnAllComments_whenPostHasComments() {
        Long postId = 2L;

        List<Comment> result = commentRepository.findByPostId(postId);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}