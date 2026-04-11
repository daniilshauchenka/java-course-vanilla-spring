package ru.yandex.springbootblog.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.exception.ExceptionType;
import ru.yandex.exception.MyException;
import ru.yandex.mapper.CommentMapper;
import ru.yandex.model.dto.CommentCreateUpdateRequest;
import ru.yandex.model.dto.CommentDto;
import ru.yandex.model.entity.CommentEntity;
import ru.yandex.repository.CommentRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository repository;

    public List<CommentDto> getCommentsByPostId(Long postId) {
        log.debug("Fetching comments for post id={}", postId);
        return repository.findByPostId(postId).stream()
            .map(CommentMapper::toDto)
            .toList();
    }

    public CommentDto getComment(Long postId, Long commentId) {
        log.debug("Fetching comment id={} for post id={}", commentId, postId);
        CommentEntity entity = getOrThrow(commentId);
        if (!entity.getPostId().equals(postId)) {
            throw new MyException(ExceptionType.COMMENT_POST_MISMATCH, commentId, postId);
        }
        return CommentMapper.toDto(entity);
    }

    @Transactional
    public CommentDto createComment(Long postId, CommentCreateUpdateRequest request) {
        log.info("Creating comment for post id={}", postId);
        CommentEntity entity = CommentMapper.toEntity(postId, request);
        Long id = repository.save(entity);
        CommentEntity saved = repository.findById(id);
        return CommentMapper.toDto(saved);
    }

    @Transactional
    public CommentDto updateComment(Long postId, Long commentId, CommentCreateUpdateRequest request) {
        log.info("Updating comment id={} for post id={}", commentId, postId);
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

    @Transactional
    public void deleteComment(Long postId, Long commentId) {
        log.info("Deleting comment id={} for post id={}", commentId, postId);
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