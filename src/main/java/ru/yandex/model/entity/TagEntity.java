package ru.yandex.model.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TagEntity {

    private Long id;
    private String name;
}