package ru.yandex.mapper;

import ru.yandex.model.dto.PostCreateRequest;
import ru.yandex.model.dto.PostDto;
import ru.yandex.model.entity.PostEntity;

public class PostMapper {
    public static PostEntity toEntity(PostCreateRequest request) {
        return PostEntity.builder()
            .title(request.getTitle())
            .text(request.getText())
            .tags(request.getTags())
            .likesCount(0)
            .commentsCount(0)
            .build();
    }

    public static PostDto toResponse(PostEntity post) {
        return PostDto.builder()
            .id(post.getId())
            .title(post.getTitle())
            .text(post.getText())
            .tags(post.getTags())
            .likesCount(post.getLikesCount())
            .commentsCount(post.getCommentsCount())
            .build();
    }
}
