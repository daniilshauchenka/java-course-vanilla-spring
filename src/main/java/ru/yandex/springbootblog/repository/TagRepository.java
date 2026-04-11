package ru.yandex.springbootblog.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        Long id = jdbc.queryForObject("SELECT NEXT VALUE FOR tags_seq", Long.class);
        jdbc.update("INSERT INTO tags(id, name) VALUES (?, ?)", id, name);
        return id;
    }

    public Map<Long, List<String>> findTagsByPostIds(List<Long> postIds) {
        if (postIds.isEmpty()) {
            return Map.of();
        }
        String inSql = postIds.stream()
            .map(id -> "?")
            .collect(Collectors.joining(","));

        String sql = """
                SELECT pt.post_id, t.name
                FROM tags t
                JOIN post_tags pt ON t.id = pt.tag_id
                WHERE pt.post_id IN (%s)
            """.formatted(inSql);

        List<Map<String, Object>> rows = jdbc.queryForList(sql, postIds.toArray());

        Map<Long, List<String>> result = new HashMap<>();

        for (Map<String, Object> row : rows) {
            Long postId = ((Number) row.get("post_id")).longValue();
            String tag = (String) row.get("name");

            result.computeIfAbsent(postId, k -> new ArrayList<>()).add(tag);
        }

        return result;
    }

    private TagEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TagEntity.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .build();
    }
}