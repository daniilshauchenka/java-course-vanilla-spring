package ru.yandex.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.model.dto.CommentCreateRequest;
import ru.yandex.model.dto.CommentDto;
import ru.yandex.model.dto.PostCreateRequest;
import ru.yandex.model.dto.PostDto;
import ru.yandex.model.dto.PostPageResponse;
import ru.yandex.service.CommentService;
import ru.yandex.service.PostService;
import ru.yandex.util.ApiPath;


@RestController
@RequestMapping(ApiPath.Comments.BASE)
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getComments(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/{commentId}")
    public CommentDto getComment(
        @PathVariable Long postId,
        @PathVariable Long commentId
    ) {
        return commentService.getComment(postId, commentId);
    }

    @PostMapping
    public CommentDto createComment(
        @PathVariable Long postId,
        @RequestBody CommentCreateRequest request
    ) {
        return commentService.createComment(postId, request);
    }

    @PutMapping("/{commentId}")
    public CommentDto updateComment(
        @PathVariable Long postId,
        @PathVariable Long commentId,
        @RequestBody CommentDto request
    ) {
        return commentService.updateComment(postId, commentId, request);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(
        @PathVariable Long postId,
        @PathVariable Long commentId
    ) {
        commentService.deleteComment(postId, commentId);
    }
}