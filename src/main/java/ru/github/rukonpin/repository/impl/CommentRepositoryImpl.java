package ru.github.rukonpin.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.github.rukonpin.model.Comment;
import ru.github.rukonpin.repository.CommentRepository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private static final Logger logger = LoggerFactory.getLogger(CommentRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_INSERT_COMMENT = """
            INSERT INTO comments (content)
            VALUES (?)
            """;

    private static final String SQL_INSERT_COMMENT_TO_POST = """
            INSERT INTO post_comments (post_id, comment_id)
            VALUES (?, ?)
            """;

    private static final String SQL_FIND_BY_POST_ID = """
            SELECT c.id, c.name, c.content, c.image_path, c.created_at
            FROM comments c
            JOIN post_comments pc ON comment_id = c.id
            JOIN posts p ON pc.post_id = p.id
            WHERE p.id = ?
            ORDER BY c.id DESC
            """;

    private static final String SQL_FIND_COMMENT_BY_ID = """
            SELECT id, name, content, image_path, created_at
            FROM comments
            WHERE id = ?
            """;

    private static final String SQL_UPDATE_COMMENT_BY_ID = """
            UPDATE comments
            SET content = ?
            WHERE id = ?
            """;

    private static final String SQL_DELETE_BY_ID = """
            DELETE FROM comments
            WHERE id = ?
            """;

    private static final String SQL_COUNT_BY_POST_ID = """
            SELECT COUNT(*)
            FROM post_comments
            WHERE post_id = ?
            """;

    @Override
    public List<Comment> findByPostId(Long id) {
        List<Comment> comments = jdbcTemplate.query(SQL_FIND_BY_POST_ID, ROW_MAPPER_COMMENT, id);
        logger.info("Загружено {} комментариев", comments.size());
        return comments;
    }

    @Override
    public Comment findById(Long id) {
        return jdbcTemplate.queryForObject(SQL_FIND_COMMENT_BY_ID, ROW_MAPPER_COMMENT, id);
    }

    @Override
    public long insertComment(Comment comment) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    SQL_INSERT_COMMENT,
                    new String[]{"id"}
            );

            ps.setString(1, comment.getContent());
            return ps;

        }, keyHolder);

        Number key = keyHolder.getKey();

        if (key == null) {
            throw new IllegalStateException("Не удалось получить id созданного комментария");
        }

        return key.longValue();
    }

    @Override
    public int updateById(Long id, Comment comment) {
        return jdbcTemplate.update(SQL_UPDATE_COMMENT_BY_ID, comment.getContent(), id);
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update(SQL_DELETE_BY_ID, id);
    }

    @Override
    public long countById(Long id) {
        Long count = jdbcTemplate.queryForObject(SQL_COUNT_BY_POST_ID, Long.class, id);

        return  count != null ? count : 0;
    }

    @Override
    public int insertCommentToPost(long postId, long commentId) {
        return jdbcTemplate.update(SQL_INSERT_COMMENT_TO_POST, postId, commentId);
    }

    private static RowMapper<Comment> ROW_MAPPER_COMMENT = (rs, rowNum) -> {
        Comment comment = new Comment();
        comment.setId(rs.getLong("id"));
        comment.setName(rs.getString("name"));
        comment.setContent(rs.getString("content"));
        comment.setImagePath(rs.getString("image_path"));
        comment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        return comment;
    };
}

