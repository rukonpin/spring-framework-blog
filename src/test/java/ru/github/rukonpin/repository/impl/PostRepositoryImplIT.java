package ru.github.rukonpin.repository.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.github.rukonpin.config.DatabaseConfig;
import ru.github.rukonpin.model.Post;
import ru.github.rukonpin.repository.PostRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        PostRepositoryImpl.class,
        DatabaseConfig.class
})
@Transactional
class PostRepositoryImplIT {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void findPost_shouldReturnPostWithTags_whenPostExists() {
        Long postId = 2L;

        Post result = postRepository.findPost(postId);

        assertNotNull(result);
        assertEquals(postId, result.getId());
        assertEquals("Результаты исследования VK Cloud о том, как компании работают с Kubernetes в России", result.getTitle());
        assertNotNull(result.getContent());
        assertTrue(result.getContent().contains("востребованный оркестратор"));

        assertNotNull(result.getTags());
        assertEquals(2, result.getTags().size());

        boolean hasKubernetes = result.getTags().stream()
                .anyMatch(tag -> "Kubernetes".equals(tag.getName()));
        boolean hasDocker = result.getTags().stream()
                .anyMatch(tag -> "Docker".equals(tag.getName()));

        assertTrue(hasKubernetes);
        assertTrue(hasDocker);

        assertEquals(8, result.getLikesCount());
        assertEquals(2, result.getCommentsCount());

        assertEquals("/static-images/kubernetes.jpeg", result.getImagePath());
    }

    @Test
    void findPost_shouldReturnNull_whenPostDoesNotExist() {
        Long postId = 777L;

        Post result = postRepository.findPost(postId);

        assertNull(result);
    }

    @Test
    @Sql("/test-post-without-tags.sql")
    void findPost_shouldReturnPostWithoutTags_whenPostHasNoTags() {
        Long postId = jdbcTemplate.queryForObject(
                "SELECT id FROM posts WHERE title = 'Одинокий пост'",
                Long.class
        );

        Post result = postRepository.findPost(postId);

        assertNotNull(result);
        assertTrue(result.getTags().isEmpty());
    }

    @Test
    void incrementLikes_shouldIncrementSuccessfully_whenPostExists() {
        Long postId = 1L;

        Post postBefore = postRepository.findPost(postId);
        int likesBefore = postBefore.getLikesCount();

        int result = postRepository.incrementLikes(postId);

        assertEquals(1, result);

        Post postAfter = postRepository.findPost(postId);
        assertEquals(likesBefore + 1, postAfter.getLikesCount());
    }

    @Test
    void findPostsPage_shouldReturnPostsWithTags_whenPageRequested() {
        int page = 0;
        int size = 5;

        List<Post> result = postRepository.findPostsPage(page, size);

        Post docker = result.stream()
                .filter(post -> post.getId().equals(1L))
                .findFirst()
                .orElse(null);

        assertNotNull(result);
        assertEquals(5, result.size());

        assertNotNull(result.getFirst().getTags());
        assertFalse(result.getFirst().getTags().isEmpty());

        if (docker != null) {
            assertEquals(1, docker.getTags().size());
        }
    }

    @Test
    void insertPost_shouldReturnGeneratedId_whenPostCreated() {
        Post newPost = new Post();
        newPost.setTitle("New Title");
        newPost.setContent("New content");
        newPost.setImagePath("/static-images/new.png");

        long generatedId = postRepository.insertPost(newPost);

        assertTrue(generatedId > 0);

        Post savedPost = postRepository.findPost(generatedId);

        assertNotNull(savedPost);
        assertEquals("New Title", savedPost.getTitle());
        assertEquals(0, savedPost.getLikesCount());
        assertEquals(0, savedPost.getCommentsCount());
    }
}