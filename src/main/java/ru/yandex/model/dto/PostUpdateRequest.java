package ru.yandex.model.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateRequest {

    private String title;
    private String text;
    private List<String> tags;
}
