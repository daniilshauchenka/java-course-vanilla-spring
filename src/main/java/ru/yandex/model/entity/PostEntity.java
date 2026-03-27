package ru.yandex.model.entity;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PostEntity {
    private Long id;

    private String title;

    private String text;

    private List<String> tags;

    private int likesCount;

    private int commentsCount;

    private LocalDateTime createdAt;

    private String imagePath;
}
