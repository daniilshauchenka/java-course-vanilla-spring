package ru.yandex.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostTagRepository {

    private final JdbcTemplate jdbc;

    public void save(Long postId, Long tagId) {
        jdbc.update(
            "INSERT INTO post_tags(post_id, tag_id) VALUES (?, ?)",
            postId,
            tagId
        );
    }

    public void deleteByPostId(Long postId) {
        jdbc.update("DELETE FROM post_tags WHERE post_id = ?", postId);
    }
}