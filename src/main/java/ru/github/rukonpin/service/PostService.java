package ru.github.rukonpin.service;

import ru.github.rukonpin.dto.command.CreatePostCommand;
import ru.github.rukonpin.dto.command.EditPostCommand;
import ru.github.rukonpin.dto.request.EditPostForm;
import ru.github.rukonpin.dto.response.PostCardResponse;
import ru.github.rukonpin.dto.response.PostDetailsResponse;
import ru.github.rukonpin.dto.response.TagResponse;

import java.util.List;

public interface PostService {
    List<PostCardResponse> getPosts(int page, int size, String searchTag);
    long getTotalPages(int size, String searchTag);
    List<TagResponse> getAllTags();
    List<TagResponse> getTagsForFilter();
    PostDetailsResponse getPost(Long id);
    EditPostForm getEditPost(Long id);
    void updatePost(Long id, EditPostCommand command);
    long createPost(CreatePostCommand command);
    void deletePost(Long id);
    void incrementLikes(Long id);
    void decrementLikes(Long id);
}
