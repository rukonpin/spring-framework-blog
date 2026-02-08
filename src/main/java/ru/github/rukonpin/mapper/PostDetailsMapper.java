package ru.github.rukonpin.mapper;

import ru.github.rukonpin.dto.response.PostDetailsResponse;
import ru.github.rukonpin.model.Post;

public class PostDetailsMapper {
    public static PostDetailsResponse toResponse(Post post) {
        return new PostDetailsResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getTags(),
                post.getImagePath(),
                post.getLikesCount()
        );
    }
}
