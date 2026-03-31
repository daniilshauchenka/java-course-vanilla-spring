package ru.yandex.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.model.entity.TagEntity;

@Repository
@RequiredArgsConstructor
public class TagRepository {

    private final JdbcTemplate jdbc;

    public TagEntity findByName(String name) {
        String sql = "SELECT * FROM tags WHERE name = ?";
        List<TagEntity> result = jdbc.query(sql, this::mapRow, name);
        return result.isEmpty() ? null : result.get(0);
    }

    public Long save(String name) {
        jdbc.update("INSERT INTO tags(name) VALUES (?)", name);
        return jdbc.queryForObject("SELECT MAX(id) FROM tags", Long.class);
    }

    public List<String> findTagsByPostId(Long postId) {
        String sql = """
            SELECT t.name
            FROM tags t
            JOIN post_tags pt ON t.id = pt.tag_id
            WHERE pt.post_id = ?
        """;

        return jdbc.queryForList(sql, String.class, postId);
    }

    private TagEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TagEntity.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .build();
    }
}