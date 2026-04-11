package ru.yandex.springbootblog.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostPageResponse {
    private List<PostDto> posts;
    private boolean hasPrev;
    private boolean hasNext;
    private int lastPage;
}
