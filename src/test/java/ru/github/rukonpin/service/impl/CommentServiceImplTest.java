package ru.github.rukonpin.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.github.rukonpin.dto.response.CommentResponse;
import ru.github.rukonpin.dto.request.CreateCommentForm;
import ru.github.rukonpin.exception.comment.CommentNotFoundException;
import ru.github.rukonpin.model.Comment;
import ru.github.rukonpin.repository.CommentRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void deleteComment_shouldThrowException_whenCommentNotFound() {
        Long commentId = 1L;

        when(commentRepository.deleteById(commentId)).thenReturn(0);

        assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment(commentId));
    }

    @Test
    void deleteComment_shouldDeleteSuccessfully_whenCommentExists() {
        Long commentId = 1L;

        when(commentRepository.deleteById(commentId)).thenReturn(1);

        assertDoesNotThrow(() -> commentService.deleteComment(commentId));
    }

    @Test
    void addCommentAndReturn_shouldReturnCommentResponse_whenCommentExists() {
        Long postId = 100L;
        Long commentId = 10L;

        CreateCommentForm form = new CreateCommentForm("Content");

        Comment saved = new Comment();
        saved.setId(commentId);
        saved.setContent("Content");
        saved.setCreatedAt(LocalDateTime.now());

        when(commentRepository.insertComment(any(Comment.class))).thenReturn(commentId);
        when(commentRepository.insertCommentToPost(eq(postId), anyLong())).thenReturn(1);
        when(commentRepository.findById(commentId)).thenReturn(saved);

        CommentResponse response = commentService.addCommentAndReturn(postId, form);

        assertNotNull(response);
        assertEquals(commentId, response.getId());
        assertEquals("Content", response.getContent());
        assertTrue(response.getCreatedAt().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));

        verify(commentRepository).insertComment(any(Comment.class));
        verify(commentRepository).insertCommentToPost(eq(postId), anyLong());
        verify(commentRepository).findById(commentId);
    }

    @Test
    void addCommentAndReturn_shouldThrowException_whenPostNotExists() {
        Long postId = 1L;

        CreateCommentForm form = new CreateCommentForm(
                "Content"
        );

        when(commentRepository.insertComment(any(Comment.class))).thenReturn(1L);
        when(commentRepository.insertCommentToPost(eq(postId), anyLong())).thenReturn(0);

        assertThrows(CommentNotFoundException.class, () -> commentService.addCommentAndReturn(postId, form));

        verify(commentRepository).insertComment(any(Comment.class));
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void updateComment_shouldUpdateSuccessfully_whenDataIsValid() {
        Long postId = 1L;
        CreateCommentForm form = new CreateCommentForm("Content");

        when(commentRepository.updateById(eq(postId), any(Comment.class))).thenReturn(1);

        assertDoesNotThrow(() -> commentService.updateComment(postId, form));
    }

    @Test
    void updateComment_shouldThrowException_whenCommentNotFound() {
        when(commentRepository.updateById(eq(1L), any(Comment.class))).thenReturn(0);

        assertThrows(CommentNotFoundException.class,
                () -> commentService.updateComment(1L, new CreateCommentForm("Content")));
    }

    @Test
    void countComments_shouldReturnCountFromRepository() {
        when(commentRepository.countById(eq(1L))).thenReturn(4L);

        long count = commentService.countComments(1L);

        assertEquals(4L, count);
    }
}