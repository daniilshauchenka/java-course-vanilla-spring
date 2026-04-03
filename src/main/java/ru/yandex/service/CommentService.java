package ru.yandex.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.exception.ExceptionType;
import ru.yandex.exception.MyException;
import ru.yandex.mapper.CommentMapper;
import ru.yandex.model.dto.CommentCreateUpdateRequest;
import ru.yandex.model.dto.CommentDto;
import ru.yandex.model.entity.CommentEntity;
import ru.yandex.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repository;

    public List<CommentDto> getCommentsByPostId(Long postId) {
        return repository.findByPostId(postId).stream()
            .map(CommentMapper::toDto)
            .toList();
    }

    public CommentDto getComment(Long postId, Long commentId) {
        CommentEntity entity = getOrThrow(commentId);
        if (!entity.getPostId().equals(postId)) {
            throw new MyException(ExceptionType.COMMENT_POST_MISMATCH, commentId, postId);
        }
        return CommentMapper.toDto(entity);
    }

    public CommentDto createComment(Long postId, CommentCreateUpdateRequest request) {
        CommentEntity entity = CommentMapper.toEntity(postId, request);
        Long id = repository.save(entity);
        CommentEntity saved = repository.findById(id);
        return CommentMapper.toDto(saved);
    }

    public CommentDto updateComment(Long postId, Long commentId, CommentCreateUpdateRequest request) {
        CommentEntity existing = getOrThrow(commentId);
        if (!existing.getPostId().equals(postId)) {
            throw new MyException(ExceptionType.COMMENT_POST_MISMATCH, commentId, postId);
        }
        CommentEntity updated = CommentEntity.builder()
            .id(commentId)
            .text(request.getText())
            .postId(postId)
            .build();
        repository.update(updated);
        return CommentMapper.toDto(repository.findById(commentId));
    }

    public void deleteComment(Long postId, Long commentId) {
        CommentEntity existing = getOrThrow(commentId);
        if (!existing.getPostId().equals(postId)) {
            throw new MyException(ExceptionType.COMMENT_POST_MISMATCH, commentId, postId);
        }
        repository.delete(commentId);
    }

    private CommentEntity getOrThrow(Long id) {
        CommentEntity entity = repository.findById(id);
        if (entity == null) {
            throw new MyException(ExceptionType.COMMENT_NOT_FOUND, id);
        }
        return entity;
    }
}