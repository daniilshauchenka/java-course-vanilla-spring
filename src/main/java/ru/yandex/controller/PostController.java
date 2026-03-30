package ru.yandex.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.model.dto.PostCreateRequest;
import ru.yandex.model.dto.PostDto;
import ru.yandex.model.dto.PostPageResponse;
import ru.yandex.service.PostService;
import ru.yandex.util.ApiPath;


@RestController
@RequestMapping(ApiPath.Posts.BASE)
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @GetMapping
    public PostPageResponse getPosts(
        @RequestParam(name = "search", defaultValue = "") String search,
        @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
        @RequestParam(name = "pageSize", defaultValue = "10") int pageSize
    ) {
        return postService.getPosts(search, pageNumber, pageSize);
    }

    @GetMapping("/{id}")
    public PostDto getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PostMapping
    public PostDto createPost(@RequestBody PostCreateRequest request) {
        return postService.createPost(request);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    @PostMapping("/{id}/likes")
    public Long incrementLikes(@PathVariable Long id) {
        return postService.incrementLikes(id);
    }

}
