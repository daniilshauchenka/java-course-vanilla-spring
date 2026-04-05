package ru.yandex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.exception.MyException;
import ru.yandex.model.dto.PostCreateRequest;
import ru.yandex.model.dto.PostDto;
import ru.yandex.model.dto.PostPageResponse;
import ru.yandex.model.entity.PostEntity;
import ru.yandex.repository.CommentRepository;
import ru.yandex.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TagService tagService;

    @InjectMocks
    private PostService postService;


    @Test
    void getPosts_shouldReturnPageResponse() {
        String search = "test";
        int page = 1;
        int size = 2;

        PostEntity post1 = PostEntity.builder()
                .id(1L)
                .title("title1")
                .text("text1")
                .likesCount(1)
                .commentsCount(1)
                .build();

        PostEntity post2 = PostEntity.builder()
                .id(2L)
                .title("title2")
                .text("text2")
                .likesCount(2)
                .commentsCount(2)
                .build();

        when(postRepository.countPosts(search)).thenReturn(2);
        when(postRepository.findPosts(search, 0, size))
                .thenReturn(List.of(post1, post2));

        when(tagService.getTagsForPosts(List.of(1L, 2L)))
                .thenReturn(Map.of(
                        1L, List.of("tag1"),
                        2L, List.of("tag2")
                ));


        PostPageResponse response = postService.getPosts(search, page, size);


        assertEquals(2, response.getPosts().size());
        assertTrue(response.isHasNext() == false);
        assertEquals(1, response.getLastPage());
    }

    @Test
    void getPosts_shouldThrowException_whenInvalidPagination() {
        assertThrows(MyException.class, () ->
                postService.getPosts("test", 0, 10)
        );
    }


    @Test
    void getPostById_shouldReturnPost() {
        PostEntity post = PostEntity.builder()
                .id(1L)
                .title("title")
                .text("text")
                .likesCount(0)
                .commentsCount(0)
                .build();

        when(postRepository.findById(1L)).thenReturn(post);
        when(tagService.getTagsForPost(1L)).thenReturn(List.of("tag"));

        PostDto  dto = postService.getPostById(1L);

        assertEquals(1L, dto.getId());
        assertEquals("tag", dto.getTags().get(0));
    }


    @Test
    void getPostById_shouldThrow_whenNotFound() {
        when(postRepository.findById(1L)).thenReturn(null);

        assertThrows(MyException.class, () ->
                postService.getPostById(1L)
        );
    }


    @Test
    void createPost_shouldCreateAndReturnPost() {
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("title");
        request.setText("text");
        request.setTags(List.of("tag1"));

        PostEntity saved = PostEntity.builder()
                .id(1L)
                .title("title")
                .text("text")
                .likesCount(0)
                .commentsCount(0)
                .build();

        when(postRepository.save(any())).thenReturn(1L);
        when(postRepository.findById(1L)).thenReturn(saved);
        when(tagService.getTagsForPost(1L)).thenReturn(List.of("tag1"));

        PostDto result = postService.createPost(request);

        assertEquals(1L, result.getId());
        verify(tagService).attachTagsToPost(eq(1L), any());
    }


    @Test
    void deletePost_shouldDelete() {
        PostEntity post = PostEntity.builder().id(1L).build();

        when(postRepository.findById(1L)).thenReturn(post);

        postService.deletePost(1L);

        verify(commentRepository).deleteByPostId(1L);
        verify(postRepository).deleteById(1L);
    }


    @Test
    void deletePost_shouldThrow_whenNotFound() {
        when(postRepository.findById(1L)).thenReturn(null);

        assertThrows(MyException.class, () ->
                postService.deletePost(1L)
        );
    }


    @Test
    void incrementLikes_shouldIncrement() {
        PostEntity post = PostEntity.builder().id(1L).build();

        when(postRepository.findById(1L)).thenReturn(post);
        when(postRepository.incrementLikes(1L)).thenReturn(5L);

        Long result = postService.incrementLikes(1L);

        assertEquals(5L, result);
    }


    @Test
    void updatePost_shouldUpdate() {
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("new");
        request.setText("new text");
        request.setTags(List.of("tag"));

        PostEntity existing = PostEntity.builder()
                .id(1L)
                .likesCount(1)
                .commentsCount(1)
                .build();

        PostEntity saved = PostEntity.builder()
                .id(1L)
                .title("new")
                .text("new text")
                .likesCount(1)
                .commentsCount(1)
                .build();

        when(postRepository.findById(1L)).thenReturn(existing);
        when(postRepository.findById(1L)).thenReturn(saved);
        when(tagService.getTagsForPost(1L)).thenReturn(List.of("tag"));

        PostDto result = postService.updatePost(1L, request);

        verify(postRepository).update(any());
        verify(tagService).replaceTags(eq(1L), any());
    }


    @Test
    void getImage_shouldThrow_whenNoImage() {
        PostEntity post = PostEntity.builder().id(1L).build();

        when(postRepository.findById(1L)).thenReturn(post);
        when(postRepository.getImage(1L)).thenReturn(null);

        assertThrows(MyException.class, () ->
                postService.getImage(1L)
        );
    }

}
