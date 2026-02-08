package ru.github.rukonpin.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.github.rukonpin.dto.command.CreatePostCommand;
import ru.github.rukonpin.dto.command.EditPostCommand;
import ru.github.rukonpin.dto.response.PostCardResponse;
import ru.github.rukonpin.dto.response.PostDetailsResponse;
import ru.github.rukonpin.exception.common.ValidationException;
import ru.github.rukonpin.exception.post.PostNotFoundException;
import ru.github.rukonpin.model.Post;
import ru.github.rukonpin.repository.PostRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    void incrementLikes_shouldThrowException_whenPostNotFound() {
        Long postId = 1L;
        when(postRepository.incrementLikes(postId)).thenReturn(0);

        assertThrows(PostNotFoundException.class, () -> postService.incrementLikes(postId));
    }

    @Test
    void incrementLikes_shouldNotThrow_whenPostExists() {
        Long postId = 1L;
        when(postRepository.incrementLikes(postId)).thenReturn(1);

        assertDoesNotThrow(() -> postService.incrementLikes(postId));
    }

    @Test
    void getPost_shouldThrowException_whenNotFound() {
        Long postId = 42L;
        when(postRepository.findPost(postId)).thenReturn(null);

        assertThrows(PostNotFoundException.class, () -> postService.getPost(postId));
    }

    @Test
    void getPost_shouldReturnPostDetails_whenPostExists() {
        Long postId = 1L;

        Post existing = new Post();
        existing.setId(postId);
        existing.setTitle("Title");
        existing.setContent("Content");

        when(postRepository.findPost(postId)).thenReturn(existing);

        PostDetailsResponse response = postService.getPost(postId);

        assertNotNull(response);
        assertEquals(postId, response.getId());
        assertEquals("Title", response.getTitle());
        assertEquals("Content", response.getContent());
    }

    @Test
    void updatePost_shouldThrowValidationException_whenTitleIsBlank() {
        EditPostCommand command = new EditPostCommand(
                " ",
                "content",
                List.of(1L, 2L),
                null
        );

        assertThrows(ValidationException.class, () -> postService.updatePost(1L, command));
        verifyNoInteractions(postRepository);
    }

    @Test
    void updatePost_shouldThrowNotFound_whenPostDoesNotExist() {
        Long postId = 10L;

        EditPostCommand command = new EditPostCommand(
                "Title",
                "content",
                List.of(1L),
                null
        );

        when(postRepository.findPost(postId)).thenReturn(null);

        assertThrows(PostNotFoundException.class, () -> postService.updatePost(postId, command));
        verify(postRepository).findPost(postId);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void updatePost_shouldUpdatePost_whenDataIsValid() {
        Long postId = 1L;

        Post existing = new Post();
        existing.setId(postId);
        existing.setImagePath("old.png");

        EditPostCommand command = new EditPostCommand(
                "Title",
                "content",
                null,
                null
        );

        when(postRepository.findPost(postId)).thenReturn(existing);
        when(postRepository.updatePost(eq(postId), any(Post.class))).thenReturn(1);

        postService.updatePost(postId, command);

        verify(postRepository).findPost(postId);
        verify(postRepository).updatePost(eq(postId), any(Post.class));
        verify(postRepository).deletePostTags(postId);
    }

    @Test
    void updatePost_shouldReplaceTags_whenTagsProvided() {
        Long postId = 1L;

        Post existing = new Post();
        existing.setId(postId);
        existing.setImagePath("img.png");

        EditPostCommand command = new EditPostCommand(
                "Title",
                "content",
                List.of(2L, 3L),
                null
        );

        when(postRepository.findPost(postId)).thenReturn(existing);
        when(postRepository.updatePost(eq(postId), any(Post.class))).thenReturn(1);

        postService.updatePost(postId, command);

        verify(postRepository).deletePostTags(postId);
        verify(postRepository).insertPostTag(postId, 2L);
        verify(postRepository).insertPostTag(postId, 3L);
    }

    @Test
    void getTotalPages_shouldReturnCorrectPages_whenCountPosts() {
        int size = 5;
        String searchTag = "Docker";
        when(postRepository.countPosts(searchTag)).thenReturn(10L);

        long totalPages = postService.getTotalPages(size, searchTag);

        assertEquals(2, totalPages);
    }

    @Test
    void getPosts_shouldReturnCorrectPreview_whenGetContent() {
        int page = 0;
        int size = 5;

        Post post = new Post();
        post.setId(1L);
        post.setContent(
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum " +
                "has been the industry's standard dummy text ever since the 1500s, when an unknown printer " +
                "took a galley of type and scrambled it to make a type specimen book. It has survived not " +
                "only five centuries, but also the leap into electronic typesetting, remaining essentially " +
                "unchanged. It was popularised in the 1960s with the release of Letraset sheets containing " +
                "Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker " +
                "including versions of Lorem Ipsum."
        );

        when(postRepository.findPostsPage(page, size)).thenReturn(List.of(post));

        List<PostCardResponse> result = postService.getPosts(page, size, null);

        PostCardResponse response = result.get(0);

        assertNotNull(response.getPreview());
        assertFalse(response.getPreview().isBlank());
        assertTrue(response.getPreview().length() < post.getContent().length());
        assertTrue(response.getPreview().endsWith("..."));
    }

    @Test
    void getPosts_shouldUserFilterMethod_whenSearchTagProvided() {
        int page = 0;
        int size = 5;
        String tag = "Docker";

        when(postRepository.findPostsPageByTag(page, size, tag)).thenReturn(List.of());

        postService.getPosts(page, size, tag);

        verify(postRepository).findPostsPageByTag(page, size, tag);
        verify(postRepository, never()).findPostsPage(page, size);
    }

    @Test
    void createPost_shouldReturnPostId_whenValidCommand() {
        Long postId = 1L;

        CreatePostCommand command = new CreatePostCommand(
                "Title",
                "Content",
                List.of(1L, 2L),
                null
        );

        Post savedPost = new Post();
        savedPost.setId(postId);

        when(postRepository.insertPost(any(Post.class))).thenReturn(postId);
        when(postRepository.findPost(postId)).thenReturn(savedPost);

        long result = postService.createPost(command);

        assertEquals(postId, result);
        verify(postRepository).insertPost(any(Post.class));
        verify(postRepository).findPost(postId);
        verify(postRepository).insertPostTag(postId, 1L);
        verify(postRepository).insertPostTag(postId, 2L);
    }

    @Test
    void createPost_shouldNoInsertTags_whenTagIdsIsNull() {
        Long postId = 1L;

        CreatePostCommand command = new CreatePostCommand(
                "Title",
                "Content",
                null,
                null
        );

        when(postRepository.insertPost(any(Post.class))).thenReturn(postId);
        when(postRepository.findPost(postId)).thenReturn(new Post());

        long result = postService.createPost(command);

        assertEquals(postId, result);
        verify(postRepository, never()).insertPostTag(eq(postId), anyLong());
    }
}
