package ru.yandex.springbootblog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.exception.MyException;
import ru.yandex.model.dto.CommentCreateUpdateRequest;
import ru.yandex.model.dto.CommentDto;
import ru.yandex.model.entity.CommentEntity;
import ru.yandex.repository.CommentRepository;
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository repository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void getCommentsByPostId_shouldReturnList() {
        CommentEntity c1 = CommentEntity.builder()
                .id(1L).text("text1").postId(1L).build();

        CommentEntity c2 = CommentEntity.builder()
                .id(2L).text("text2").postId(1L).build();

        when(repository.findByPostId(1L)).thenReturn(List.of(c1, c2));

        List<CommentDto> result = commentService.getCommentsByPostId(1L);

        assertEquals(2, result.size());
        assertEquals("text1", result.get(0).getText());
    }

    @Test
    void getComment_shouldReturnComment() {
        CommentEntity entity = CommentEntity.builder()
                .id(1L).text("text").postId(1L).build();

        when(repository.findById(1L)).thenReturn(entity);

        CommentDto result = commentService.getComment(1L, 1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getComment_shouldThrow_whenPostMismatch() {
        CommentEntity entity = CommentEntity.builder()
                .id(1L).text("text").postId(2L).build();

        when(repository.findById(1L)).thenReturn(entity);

        assertThrows(MyException.class, () ->
                commentService.getComment(1L, 1L)
        );
    }

    @Test
    void getComment_shouldThrow_whenNotFound() {
        when(repository.findById(1L)).thenReturn(null);

        assertThrows(MyException.class, () ->
                commentService.getComment(1L, 1L)
        );
    }

    @Test
    void createComment_shouldCreate() {
        CommentCreateUpdateRequest request = new CommentCreateUpdateRequest();
        request.setText("text");

        CommentEntity saved = CommentEntity.builder()
                .id(1L).text("text").postId(1L).build();

        when(repository.save(any())).thenReturn(1L);
        when(repository.findById(1L)).thenReturn(saved);

        CommentDto result = commentService.createComment(1L, request);

        assertEquals(1L, result.getId());
    }

    @Test
    void updateComment_shouldUpdate() {
        CommentCreateUpdateRequest request = new CommentCreateUpdateRequest();
        request.setText("updated");

        CommentEntity existing = CommentEntity.builder()
                .id(1L).text("old").postId(1L).build();

        CommentEntity updated = CommentEntity.builder()
                .id(1L).text("updated").postId(1L).build();

        when(repository.findById(1L)).thenReturn(existing, updated);

        CommentDto result = commentService.updateComment(1L, 1L, request);

        assertEquals("updated", result.getText());
        verify(repository).update(any());
    }

    @Test
    void updateComment_shouldThrow_whenMismatch() {
        CommentCreateUpdateRequest request = new CommentCreateUpdateRequest();
        request.setText("text");

        CommentEntity existing = CommentEntity.builder()
                .id(1L).postId(2L).build();

        when(repository.findById(1L)).thenReturn(existing);

        assertThrows(MyException.class, () ->
                commentService.updateComment(1L, 1L, request)
        );
    }


    @Test
    void updateComment_shouldThrow_whenNotFound() {
        when(repository.findById(1L)).thenReturn(null);

        CommentCreateUpdateRequest request = new CommentCreateUpdateRequest();

        assertThrows(MyException.class, () ->
                commentService.updateComment(1L, 1L, request)
        );
    }

    @Test
    void deleteComment_shouldDelete() {
        CommentEntity entity = CommentEntity.builder()
                .id(1L).postId(1L).build();

        when(repository.findById(1L)).thenReturn(entity);

        commentService.deleteComment(1L, 1L);

        verify(repository).delete(1L);
    }

    @Test
    void deleteComment_shouldThrow_whenMismatch() {
        CommentEntity entity = CommentEntity.builder()
                .id(1L).postId(2L).build();

        when(repository.findById(1L)).thenReturn(entity);

        assertThrows(MyException.class, () ->
                commentService.deleteComment(1L, 1L)
        );
    }

    @Test
    void deleteComment_shouldThrow_whenNotFound() {
        when(repository.findById(1L)).thenReturn(null);

        assertThrows(MyException.class, () ->
                commentService.deleteComment(1L, 1L)
        );
    }
}