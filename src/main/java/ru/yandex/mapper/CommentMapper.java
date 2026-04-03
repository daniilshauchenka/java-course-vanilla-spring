package ru.yandex.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.model.dto.CommentCreateUpdateRequest;
import ru.yandex.model.dto.CommentDto;
import ru.yandex.model.entity.CommentEntity;

@UtilityClass
public class CommentMapper {

    public static CommentEntity toEntity(Long postId, CommentCreateUpdateRequest request) {
        return CommentEntity.builder()
            .text(request.getText())
            .postId(postId)
            .build();
    }

    public static CommentDto toDto(CommentEntity entity) {
        return CommentDto.builder()
            .id(entity.getId())
            .text(entity.getText())
            .postId(entity.getPostId())
            .build();
    }
}