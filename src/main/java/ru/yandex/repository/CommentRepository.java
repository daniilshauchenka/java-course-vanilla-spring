package ru.yandex.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.model.entity.CommentEntity;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final JdbcTemplate jdbc;

    public Long save(CommentEntity comment) {
        String sql = """
            INSERT INTO comments(text, post_id)
            VALUES (?, ?)
        """;

        jdbc.update(sql, comment.getText(), comment.getPostId());

        return jdbc.queryForObject("SELECT MAX(id) FROM comments", Long.class);
    }

    public List<CommentEntity> findByPostId(Long postId) {
        String sql = "SELECT * FROM comments WHERE post_id = ?";

        return jdbc.query(sql, this::mapRow, postId);
    }

    public CommentEntity findById(Long commentId) {
        String sql = "SELECT * FROM comments WHERE id = ?";

        return jdbc.queryForObject(sql, this::mapRow, commentId);
    }

    public void delete(Long commentId) {
        jdbc.update("DELETE FROM comments WHERE id = ?", commentId);
    }

    public void update(CommentEntity comment) {
        String sql = """
            UPDATE comments
            SET text = ?
            WHERE id = ?
        """;

        jdbc.update(sql, comment.getText(), comment.getId());
    }

    private CommentEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return CommentEntity.builder()
            .id(rs.getLong("id"))
            .text(rs.getString("text"))
            .postId(rs.getLong("post_id"))
            .build();
    }
}