package ru.yandex.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.mapper.CommentMapper;
import ru.yandex.model.dto.CommentCreateRequest;
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
        CommentEntity entity = repository.findById(commentId);
        if (!entity.getPostId().equals(postId)) {
            throw new IllegalArgumentException("Comment does not belong to post");
        }
        return CommentMapper.toDto(entity);
    }

    public CommentDto createComment(Long postId, CommentCreateRequest request) {
        CommentEntity entity = CommentMapper.toEntity(postId, request);
        Long id = repository.save(entity);
        CommentEntity saved = repository.findById(id);
        return CommentMapper.toDto(saved);
    }

    public CommentDto updateComment(Long postId, Long commentId, CommentDto request) {
        CommentEntity existing = repository.findById(commentId);
        if (!existing.getPostId().equals(postId)) {
            throw new IllegalArgumentException("Comment does not belong to post");
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
        CommentEntity existing = repository.findById(commentId);
        if (!existing.getPostId().equals(postId)) {
            throw new IllegalArgumentException("Comment does not belong to post");
        }
        repository.delete(commentId);
    }
}