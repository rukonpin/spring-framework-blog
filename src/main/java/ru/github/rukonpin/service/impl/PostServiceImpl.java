package ru.github.rukonpin.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.github.rukonpin.dto.command.CreatePostCommand;
import ru.github.rukonpin.dto.command.EditPostCommand;
import ru.github.rukonpin.dto.request.EditPostForm;
import ru.github.rukonpin.dto.response.PostCardResponse;
import ru.github.rukonpin.dto.response.PostDetailsResponse;
import ru.github.rukonpin.dto.response.TagResponse;
import ru.github.rukonpin.exception.common.ValidationException;
import ru.github.rukonpin.exception.post.PostNotFoundException;
import ru.github.rukonpin.mapper.PostCardMapper;
import ru.github.rukonpin.mapper.PostDetailsMapper;
import ru.github.rukonpin.mapper.TagMapper;
import ru.github.rukonpin.model.Post;
import ru.github.rukonpin.model.Tag;
import ru.github.rukonpin.repository.PostRepository;
import ru.github.rukonpin.service.PostService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {this.postRepository = postRepository;}

    @Override
    public List<PostCardResponse> getPosts(int page, int size, String searchTag) {

        List<Post> posts = new ArrayList<>();

        if (searchTag == null || searchTag.isBlank()) {
            posts = postRepository.findPostsPage(page, size);
        } else {
            posts = postRepository.findPostsPageByTag(page, size, searchTag);
        }

        for (Post post : posts) {
            String preview = buildPreview(post.getContent());
            post.setPreview(preview);
        }

        return PostCardMapper.toResponseList(posts);
    }

    @Override
    public long getTotalPages(int size, String searchTag) {
        long totalPosts = postRepository.countPosts(searchTag);
        return (totalPosts + size - 1) / size;
    }

    @Override
    public List<TagResponse> getAllTags() {
        List<Tag> tags = postRepository.findAllTags();
        return TagMapper.toResponseList(tags);
    }

    @Override
    public List<TagResponse> getTagsForFilter() {
        List<Tag> tags = postRepository.findTagsWithPosts();
        return TagMapper.toResponseList(tags);
    }

    @Override
    public void incrementLikes(Long id) {
        int likes = postRepository.incrementLikes(id);

        if (likes == 0) {
            throw new PostNotFoundException(id);
        }
    }

    @Override
    public void decrementLikes(Long id) {
        int likes = postRepository.decrementLikes(id);

        if (likes == 0) {
            throw new PostNotFoundException(id);
        }
    }

    @Override
    public PostDetailsResponse getPost(Long id) {
        Post post = postRepository.findPost(id);

        if (post == null) {
            throw new PostNotFoundException(id);
        }

        return PostDetailsMapper.toResponse(post);
    }

    @Override
    public long createPost(CreatePostCommand command) {
        Post post = new Post();
        post.setTitle(command.getTitle());
        post.setContent(command.getContent());
        post.setImagePath(command.getImagePath());

        long postId = postRepository.insertPost(post);

        String preview = buildPreview(command.getContent());
        Post savedPost = postRepository.findPost(postId);
        savedPost.setPreview(preview);

        if (command.getTagIds() != null) {
            for (Long tagId : command.getTagIds()) {
                postRepository.insertPostTag(postId, tagId);
            }
        }

        return postId;
    }

    @Override
    public EditPostForm getEditPost(Long id) {

        Post post = postRepository.findPost(id);

        if (post == null) {
            throw new PostNotFoundException(id);
        }

        logger.info("POST TAGS = {}", post.getTags());

        EditPostForm form = new EditPostForm();
        form.setTitle(post.getTitle());
        form.setContent(post.getContent());
        form.setTagIds(post.getTags().stream().map(Tag::getId).toList());

        return form;
    }

    @Override
    public void updatePost(Long id, EditPostCommand command) {
        if (command.getTitle() == null || command.getTitle().isBlank()) {
            throw new ValidationException("Заголовок не может быть пустым");
        }

        Post existing = postRepository.findPost(id);

        if (existing == null) {
            throw new PostNotFoundException(id);
        }

        Post post = new Post();
        post.setTitle(command.getTitle());
        post.setContent(command.getContent());
        post.setPreview(buildPreview(command.getContent()));

        if (command.getImagePath() != null) {
            post.setImagePath(command.getImagePath());
        } else {
            post.setImagePath(existing.getImagePath());
        }

        int updated = postRepository.updatePost(id, post);
        if (updated == 0) {
            throw new PostNotFoundException(id);
        }

        postRepository.deletePostTags(id);

        if (command.getTagIds() != null) {
            for (Long tagId : command.getTagIds()) {
                postRepository.insertPostTag(id, tagId);
            }
        }
    }

    @Override
    public void deletePost(Long id) {
        int deleted = postRepository.deletePost(id);

        if (deleted == 0) {
            throw new PostNotFoundException(id);
        }
    }

    public static String buildPreview(String content) {
        if (content == null || content.isBlank()) {
            return "";
        }

        int maxWords = 30;
        String[] words = content.split("\\s+");

        if (words.length <= maxWords) {
            return content;
        }

        return String.join(" ", Arrays.copyOfRange(words, 0, maxWords)) + "...";
    }
}
