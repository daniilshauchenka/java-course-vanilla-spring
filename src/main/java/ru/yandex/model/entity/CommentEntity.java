package ru.yandex.model.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentEntity {

    private Long id;
    private String text;
    private Long postId;
}