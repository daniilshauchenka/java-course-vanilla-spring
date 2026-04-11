package ru.yandex.springbootblog.mapper;

import java.util.List;
import lombok.experimental.UtilityClass;
import ru.yandex.model.dto.PostCreateRequest;
import ru.yandex.model.dto.PostDto;
import ru.yandex.model.entity.PostEntity;

@UtilityClass
public class PostMapper {
    public static PostEntity toEntityFromCreateRequest(PostCreateRequest request) {
        return PostEntity.builder()
            .title(request.getTitle())
            .text(request.getText())
            .tags(request.getTags())
            .likesCount(0)
            .commentsCount(0)
            .build();
    }

    public static PostDto toDto(PostEntity post, List<String> tags) {
        return PostDto.builder()
            .id(post.getId())
            .title(post.getTitle())
            .text(post.getText())
            .tags(tags)
            .likesCount(post.getLikesCount())
            .commentsCount(post.getCommentsCount())
            .build();
    }
}
