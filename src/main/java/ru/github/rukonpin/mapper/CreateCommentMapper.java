package ru.github.rukonpin.mapper;

import ru.github.rukonpin.dto.response.CommentResponse;
import ru.github.rukonpin.model.Comment;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CreateCommentMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static CommentResponse toResponse(Comment comment) {
        String createdAtStr = comment.getCreatedAt().format(FORMATTER);

        return new CommentResponse(
                comment.getId(),
                comment.getName(),
                comment.getContent(),
                comment.getImagePath(),
                createdAtStr
        );
    }

    public static List<CommentResponse> toResponseList(List<Comment> comments) {
        return comments.stream()
                .map(CreateCommentMapper::toResponse)
                .toList();
    }
}
