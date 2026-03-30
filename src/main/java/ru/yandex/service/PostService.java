package ru.yandex.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.mapper.PostMapper;
import ru.yandex.model.dto.PostCreateRequest;
import ru.yandex.model.dto.PostDto;
import ru.yandex.model.dto.PostPageResponse;
import ru.yandex.model.entity.PostEntity;
import ru.yandex.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


    public PostPageResponse getPosts(String search, int pageNumber, int pageSize) {

        int total = postRepository.countPosts(search);
        int offset = (pageNumber - 1) * pageSize;

        List<PostEntity> posts = postRepository.findPosts(search, offset, pageSize);

        List<PostDto> dtos = posts.stream()
            .map(PostMapper::toDto)
            .map(this::truncateText)
            .toList();

        int lastPage = (int) Math.ceil((double) total / pageSize);

        return PostPageResponse.builder()
            .posts(dtos)
            .hasPrev(pageNumber > 1)
            .hasNext(pageNumber < lastPage)
            .lastPage(lastPage)
            .build();
    }

    public PostDto getPostById(Long id) {
        PostEntity post = postRepository.findById(id);
        return PostMapper.toDto(post);
    }


    public PostDto createPost(PostCreateRequest createRequest) {
        PostEntity post = PostMapper.toEntityFromCreateRequest(createRequest);
        Long id = postRepository.save(post);
        PostEntity saved = postRepository.findById(id);
        return PostMapper.toDto(saved);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public Long incrementLikes(Long id) {
        return postRepository.incrementLikes(id);
    }

    private PostDto truncateText(PostDto dto) {
        if (dto.getText() != null && dto.getText().length() > 128) {
            dto.setText(dto.getText().substring(0, 128) + "...");
        }
        return dto;
    }
}
