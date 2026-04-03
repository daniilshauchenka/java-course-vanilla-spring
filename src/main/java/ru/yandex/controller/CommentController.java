package ru.yandex.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.model.dto.CommentCreateUpdateRequest;
import ru.yandex.model.dto.CommentDto;
import ru.yandex.service.CommentService;
import ru.yandex.util.ApiPath;


@RestController
@RequestMapping(ApiPath.Comments.BASE)
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getComments(
        @PathVariable("postId") Long postId
    ) {
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/{commentId}")
    public CommentDto getComment(
        @PathVariable("postId") Long postId,
        @PathVariable("commentId") Long commentId
    ) {
        return commentService.getComment(postId, commentId);
    }

    @PostMapping
    public CommentDto createComment(
        @PathVariable("postId") Long postId,
        @Valid @RequestBody CommentCreateUpdateRequest request
    ) {
        return commentService.createComment(postId, request);
    }

    @PutMapping("/{commentId}")
    public CommentDto updateComment(
        @PathVariable("postId")  Long postId,
        @PathVariable("commentId")  Long commentId,
        @Valid @RequestBody CommentCreateUpdateRequest request
    ) {
        return commentService.updateComment(postId, commentId, request);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(
        @PathVariable("postId")  Long postId,
        @PathVariable("commentId")  Long commentId
    ) {
        commentService.deleteComment(postId, commentId);
    }
}