package ru.yandex.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.model.entity.PostEntity;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final JdbcTemplate jdbc;

    public Long save(PostEntity post) {

        Long id = jdbc.queryForObject("SELECT NEXT VALUE FOR post_seq", Long.class);

        String sql = """
            INSERT INTO posts(id, title, text, likes_count, comments_count)
            VALUES (?, ?, ?, ?, ?)
            """;

        jdbc.update(sql,
            id,
            post.getTitle(),
            post.getText(),
            post.getLikesCount(),
            post.getCommentsCount()
        );

        return id;
    }

    public List<PostEntity> findPosts(String search, int offset, int limit) {
        String sql = """
        SELECT *
        FROM posts
        WHERE LOWER(title) LIKE LOWER(?)
        ORDER BY id DESC
        LIMIT ? OFFSET ?
        """;

        return jdbc.query(sql, this::mapRow,
            "%" + search + "%",
            limit,
            offset
        );
    }

    public int countPosts(String search) {
        String sql = """
        SELECT COUNT(*)
        FROM posts
        WHERE LOWER(title) LIKE LOWER(?)
        """;

        return jdbc.queryForObject(sql, Integer.class, "%" + search + "%");
    }

    public PostEntity findById(Long id) {
        String sql = "SELECT * FROM posts WHERE id = ?";

        return jdbc.queryForObject(sql, this::mapRow, id);
    }

    public boolean deleteById(Long id) {
        String sql = "DELETE FROM posts WHERE id = ?";
        return jdbc.update(sql, id) > 0;
    }

    public Long incrementLikes(Long id) {
        String sql = "UPDATE posts SET likes_count = likes_count + 1 WHERE id = ?";
        jdbc.update(sql, id);
        return getLikesCount(id);
    }

    public Long getLikesCount(Long id) {
        String sql = "SELECT likes_count FROM posts WHERE id = ?";
        return jdbc.queryForObject(sql, Long.class, id);
    }

    public List<PostEntity> findAll() {
        return jdbc.query("SELECT * FROM posts", this::mapRow);
    }

    private PostEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PostEntity.builder()
            .id(rs.getLong("id"))
            .title(rs.getString("title"))
            .text(rs.getString("text"))
            .likesCount(rs.getInt("likes_count"))
            .commentsCount(rs.getInt("comments_count"))
            .build();
    }
}
