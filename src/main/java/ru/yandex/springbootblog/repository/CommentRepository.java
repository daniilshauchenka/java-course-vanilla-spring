package ru.yandex.springbootblog.repository;

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
        Long id = jdbc.queryForObject("SELECT NEXT VALUE FOR comments_seq", Long.class);
        jdbc.update("""
                INSERT INTO comments(id, text, post_id)
                VALUES (?, ?, ?)
            """, id, comment.getText(), comment.getPostId());
        return id;
    }

    public List<CommentEntity> findByPostId(Long postId) {
        String sql = "SELECT * FROM comments WHERE post_id = ?";

        return jdbc.query(sql, this::mapRow, postId);
    }

    public CommentEntity findById(Long commentId) {
        String sql = "SELECT * FROM comments WHERE id = ?";

        List<CommentEntity> result = jdbc.query(sql, this::mapRow, commentId);
        return result.isEmpty() ? null : result.getFirst();
    }

    public void delete(Long commentId) {
        jdbc.update("DELETE FROM comments WHERE id = ?", commentId);
    }

    public void deleteByPostId(Long postId) {
        jdbc.update("DELETE FROM comments WHERE post_id = ?", postId);
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