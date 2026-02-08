package ru.github.rukonpin.repository;

import ru.github.rukonpin.model.Post;
import ru.github.rukonpin.model.Tag;

import java.util.List;

public interface PostRepository {
    List<Post> findPostsPage(int page, int size);
    List<Post> findPostsPageByTag(int page, int size, String searchTag);
    List<Tag> findAllTags();
    List<Tag> findTagsWithPosts();
    Post findPost(Long id);
    long insertPost(Post post);
    int updatePost(Long id, Post post);
    void deletePostTags(Long postId);
    void insertPostTag(long postId, long tagId);
    int incrementLikes(Long id);
    int decrementLikes(Long id);
    long countPosts(String searchTag);
    int deletePost(Long id);
}
