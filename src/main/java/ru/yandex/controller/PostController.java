package ru.yandex.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.model.dto.ImageDto;
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
        @RequestParam(name = "search") String search,
        @RequestParam(name = "pageNumber") int pageNumber,
        @RequestParam(name = "pageSize") int pageSize
    ) {
        return postService.getPosts(search, pageNumber, pageSize);
    }

    @GetMapping("/{id}")
    public PostDto getPostById(@PathVariable("id") Long id) {
        return postService.getPostById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(
        @Valid @RequestBody PostCreateRequest request
    ) {
        return postService.createPost(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
    }

    @PostMapping("/{id}/likes")
    public Long incrementLikes(@PathVariable("id") Long id) {
        return postService.incrementLikes(id);
    }


    @PutMapping("/{id}/image")
    @ResponseStatus(HttpStatus.OK)
    public void uploadImage(
        @PathVariable("id") Long id,
        @RequestParam("image") MultipartFile file
    ) {
        postService.uploadImage(id, file);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        ImageDto image = postService.getImage(id);
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(image.getContentType()))
            .body(image.getData());
    }

    @PutMapping("/{id}")
    public PostDto updatePost(
        @PathVariable("id") Long id,
        @Valid @RequestBody PostCreateRequest request
    ) {
        return postService.updatePost(id, request);
    }
}
