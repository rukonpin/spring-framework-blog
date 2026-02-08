package ru.github.rukonpin.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.github.rukonpin.model.Post;
import ru.github.rukonpin.model.Tag;
import ru.github.rukonpin.repository.PostRepository;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private static final Logger logger = LoggerFactory.getLogger(PostRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_GET_POSTS_WITH_TAGS = """
            SELECT
                p.id,
                p.title,
                p.content,
                p.likes_count,
                p.image_path,
                t.name AS tag_name,
                t.icon_svg AS tag_icon_svg,
                COUNT(pc.comment_id) AS comments_count
            FROM posts p
            LEFT JOIN post_tags pt ON pt.post_id = p.id
            LEFT JOIN tags t ON pt.tag_id = t.id
            LEFT JOIN post_comments pc ON pc.post_id = p.id
            WHERE p.id IN (
                SELECT p.id
                FROM posts p
                ORDER BY p.id
                LIMIT ? OFFSET ?
                )
            GROUP BY p.id, p.title, p.content, p.likes_count, p.image_path, t.id, t.name, t.icon_svg
            ORDER BY p.id, t.id
            """;

    private static final String SQL_GET_POSTS_WITH_FILTER = """
            SELECT
                p.id,
                p.title,
                p.content,
                p.likes_count,
                p.image_path,
                t.name AS tag_name,
                t.icon_svg AS tag_icon_svg,
                COUNT(pc.comment_id) AS comments_count
            FROM posts p
            LEFT JOIN post_tags pt ON pt.post_id = p.id
            LEFT JOIN tags t ON pt.tag_id = t.id
            LEFT JOIN post_comments pc ON pc.post_id = p.id
            WHERE p.id IN (
                SELECT p.id
                FROM posts p
                JOIN post_tags pt ON pt.post_id = p.id
                JOIN tags t ON t.id = pt.tag_id
                WHERE t.name = ?
                ORDER BY p.id
                LIMIT ? OFFSET ?
                )
            GROUP BY p.id, p.title, p.content, p.likes_count, p.image_path, t.id, t.name, t.icon_svg
            ORDER BY p.id, t.id
            """;

    private static final String SQL_GET_ALL_TAGS = """
            SELECT t.id, t.name, t.icon_svg
            FROM tags t
            """;

    private static final String SQL_GET_TAG_WITH_POSTS = """
            SELECT t.id, t.name, t.icon_svg
            FROM tags t
            JOIN post_tags pt ON pt.tag_id = t.id
            GROUP BY t.id, t.name, t.icon_svg
            """;

    private static final String SQL_INCREMENT_LIKES = """
            UPDATE posts
            SET likes_count = likes_count + 1
            WHERE id = ?
            """;

    private static final String SQL_DECREMENT_LIKES = """
            UPDATE posts
            SET likes_count = GREATEST(likes_count - 1, 0)
            WHERE id = ?
            """;

    private static final String SQL_GET_COUNT_POSTS = """
            SELECT COUNT(*)
            FROM posts
            """;

    private static final String SQL_GET_FILTER_COUNT_POSTS = """
            SELECT COUNT(DISTINCT p.id)
            FROM posts p
            JOIN post_tags pt ON pt.post_id = p.id
            JOIN tags t ON t.id = pt.tag_id
            WHERE t.name = ?
            """;

    private static final String SQL_GET_POST = """
            SELECT
                p.id,
                p.title,
                p.content,
                p.image_path,
                p.comments_count,
                p.likes_count,
                t.name AS tag_name,
                t.icon_svg AS tag_icon_svg
            FROM posts p
            LEFT JOIN post_tags pt ON pt.post_id = p.id
            LEFT JOIN tags t ON pt.tag_id = t.id
            WHERE p.id = ?
            ORDER BY p.id, t.id
            """;

    private static final String SQL_CREATE_POST = """
            INSERT INTO posts (title, content, image_path)
            VALUES (?, ?, ?)
            """;

    private static final String SQL_INSERT_POST_TAG = """
            INSERT INTO post_tags(post_id, tag_id)
            VALUES (?, ?)
            """;

    private static final String SQL_UPDATE_POST = """
            UPDATE posts
            SET title=?, content=?, image_path=?
            WHERE id=?
            """;

    private static final String SQL_DELETE_POST = """
            DELETE
            FROM posts
            WHERE id = ?
            """;

    private static final String SQL_DELETE_POST_TAGS = """
            DELETE FROM post_tags 
            WHERE post_id = ?
            """;

    // NOTE: дополнительный запрос используется только для логирования
    // В реальных проектах его можно убрать, чтобы избежать лишнего обращения к БД
    private static final String SQL_GET_LIKES_COUNT_FOR_LOGGER = """
            SELECT p.likes_count
            FROM posts p
            WHERE p.id = ?
            """;

    @Override
    public List<Post> findPostsPage(int page, int size) {
        int offset = page * size;
        List<Post> posts = jdbcTemplate.query(SQL_GET_POSTS_WITH_TAGS, ROW_MAPPER_POSTS, size, offset);

        return collapsePosts(posts);
    }

    @Override
    public List<Post> findPostsPageByTag(int page, int size, String searchTag) {
        int offset = page * size;
        List<Post> posts = jdbcTemplate.query(SQL_GET_POSTS_WITH_FILTER, ROW_MAPPER_POSTS, searchTag, size, offset);

        return collapsePosts(posts);
    }

    @Override
    public List<Tag> findAllTags() {
        return jdbcTemplate.query(SQL_GET_ALL_TAGS, ROM_MAPPER_TAGS);
    }

    @Override
    public List<Tag> findTagsWithPosts() {
        return jdbcTemplate.query(SQL_GET_TAG_WITH_POSTS, ROM_MAPPER_TAGS);
    }

    @Override
    public int incrementLikes(Long id) {
        return jdbcTemplate.update(SQL_INCREMENT_LIKES, id);

//        Integer currentLikes = jdbcTemplate.queryForObject(SQL_GET_LIKES_COUNT_FOR_LOGGER, Integer.class, id);
//        logger.info("Пост id={}: количество лайков обновлено до {}", id, currentLikes);
    }

    @Override
    public int decrementLikes(Long id) {
        return jdbcTemplate.update(SQL_DECREMENT_LIKES, id);

//        Integer currentLikes = jdbcTemplate.queryForObject(SQL_GET_LIKES_COUNT_FOR_LOGGER, Integer.class, id);
//        logger.info("Пост id={}: количество лайков обновлено до {}", id, currentLikes);
    }

    @Override
    public long countPosts(String searchTag) {
        Long count = 0L;

        if (searchTag == null || searchTag.isBlank()) {
            count = jdbcTemplate.queryForObject(SQL_GET_COUNT_POSTS, Long.class);
            logger.info("Найдено всего {} постов", count);
        } else {
            count = jdbcTemplate.queryForObject(SQL_GET_FILTER_COUNT_POSTS, Long.class, searchTag);
            logger.info("Найдено с фильтром {} пост[а/ов]", count);
        }

        return count;
    }

    @Override
    public Post findPost(Long id) {
        List<Post> post = jdbcTemplate.query(SQL_GET_POST, ROW_MAPPER_POSTS, id);
        List<Post> result = collapsePosts(post);

        return result.isEmpty() ? null : result.getFirst();
    }

    @Override
    public long insertPost(Post post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    SQL_CREATE_POST,
                    //Statement.RETURN_GENERATED_KEYS
                    new String[]{"id"}
            );

            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContent());
            ps.setString(3, post.getImagePath());

            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();

        if (key == null) {
            throw new IllegalStateException("Не удалось получить id созданного поста");
        }

        return key.longValue();
    }

    @Override
    public void insertPostTag(long postId, long tagId) {
        jdbcTemplate.update(SQL_INSERT_POST_TAG, postId, tagId);
    }

    @Override
    public int updatePost(Long id, Post post) {
        return jdbcTemplate.update(SQL_UPDATE_POST, post.getTitle(), post.getContent(), post.getImagePath(), id);
    }

    @Override
    public void deletePostTags(Long postId) {
        jdbcTemplate.update(SQL_DELETE_POST_TAGS, postId);
    }

    @Override
    public int deletePost(Long id) {
        return jdbcTemplate.update(SQL_DELETE_POST, id);
    }

    private static RowMapper<Post> ROW_MAPPER_POSTS = (rs, rowNum) -> {
        Post post = new Post();
        post.setId(rs.getLong("id"));
        post.setTitle(rs.getString("title"));
        post.setContent(rs.getString("content"));
        post.setLikesCount(rs.getInt("likes_count"));
        post.setCommentsCount(rs.getInt("comments_count"));
        post.setImagePath(rs.getString("image_path"));

        String tagName = rs.getString("tag_name");
        String tagIconSvg = rs.getString("tag_icon_svg");

        if (tagName != null) {
            Tag tag = new Tag();
            tag.setName(tagName);
            tag.setIconSvg(tagIconSvg);
            post.getTags().add(tag);
        }

        return post;
    };

    private static RowMapper<Tag> ROM_MAPPER_TAGS = (rs, rowNum) -> {
        Tag tag = new Tag();
        tag.setId(rs.getLong("id"));
        tag.setName(rs.getString("name"));
        tag.setIconSvg(rs.getString("icon_svg"));
        return tag;
    };

    private List<Post> collapsePosts(List<Post> posts) {
        Map<Long, Post> postMap = new LinkedHashMap<>();

        for (Post post : posts) {
            Long id = post.getId();
            if (id == null) continue;

            if (!postMap.containsKey(id)) {
                postMap.put(id, post);
            } else {
                Post existingPost = postMap.get(id);
                existingPost.getTags().addAll(post.getTags());
            }
        }

        return new ArrayList<>(postMap.values());
    }
}