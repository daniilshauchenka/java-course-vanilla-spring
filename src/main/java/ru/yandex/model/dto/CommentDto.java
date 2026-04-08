package ru.yandex.model.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentDto {

    private Long id;
    private String text;
    private Long postId;
}