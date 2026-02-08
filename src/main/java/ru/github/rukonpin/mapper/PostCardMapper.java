package ru.github.rukonpin.mapper;

import ru.github.rukonpin.dto.response.PostCardResponse;
import ru.github.rukonpin.model.Post;

import java.util.List;

public class PostCardMapper {

    public static PostCardResponse toResponse(Post post) {
        return new PostCardResponse(
                post.getId(),
                post.getTitle(),
                post.getPreview() != null ? post.getPreview() : "",
                post.getTags(),
                post.getImagePath(),
                post.getLikesCount(),
                post.getCommentsCount()
        );
    }

    public static List<PostCardResponse> toResponseList(List<Post> posts) {
        return posts.stream()
                .map(PostCardMapper::toResponse)
                .toList();
    }
}
