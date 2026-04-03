package ru.yandex.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CommentCreateUpdateRequest {

    @NotEmpty
    private String text;
}