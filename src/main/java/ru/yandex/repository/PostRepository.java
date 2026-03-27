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
        String sql = """
                INSERT INTO posts(title, text, likes_count, comments_count)
                VALUES (?, ?, ?, ?)
                """;

        jdbc.update(sql,
            post.getTitle(),
            post.getText(),
            post.getLikesCount(),
            post.getCommentsCount()
        );

        return jdbc.queryForObject("SELECT MAX(id) FROM posts", Long.class);
    }

    public PostEntity findById(Long id) {
        String sql = "SELECT * FROM posts WHERE id = ?";

        return jdbc.queryForObject(sql, this::mapRow, id);
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
