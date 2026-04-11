package ru.yandex.springbootblog.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateUpdateRequest {

    @NotEmpty
    private String text;
}