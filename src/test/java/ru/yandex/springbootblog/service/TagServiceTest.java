package ru.yandex.springbootblog.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.model.entity.TagEntity;
import ru.yandex.repository.PostTagRepository;
import ru.yandex.repository.TagRepository;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {


    @Mock
    private TagRepository tagRepository;

    @Mock
    private PostTagRepository postTagRepository;

    @InjectMocks
    private TagService tagService;

    @Test
    void shouldAttachNewTagsToPost() {

        Long postId = 1L;
        List<String> tags = List.of("tag1", "tag2");

        when(tagRepository.findByName("tag1")).thenReturn(null);
        when(tagRepository.findByName("tag2")).thenReturn(null);

        when(tagRepository.save("tag1")).thenReturn(1L);
        when(tagRepository.save("tag2")).thenReturn(2L);

        tagService.attachTagsToPost(postId, tags);

        verify(postTagRepository).save(postId, 1L);
        verify(postTagRepository).save(postId, 2L);
    }

    @Test
    void shouldUseExistingTags() {
        Long postId = 1L;
        List<String> tags = List.of("tag1");

        TagEntity existing = new TagEntity();
        existing.setId(10L);
        existing.setName("tag1");

        when(tagRepository.findByName("tag1")).thenReturn(existing);

        tagService.attachTagsToPost(postId, tags);

        verify(postTagRepository).save(postId, 10L);
        verify(tagRepository, never()).save(any());
    }

    @Test
    void shouldRemoveDuplicates() {

        Long postId = 1L;
        List<String> tags = List.of("tag1", "tag1");

        when(tagRepository.findByName("tag1")).thenReturn(null);
        when(tagRepository.save("tag1")).thenReturn(1L);

        tagService.attachTagsToPost(postId, tags);


        verify(postTagRepository, times(1)).save(postId, 1L);
    }

    @Test
    void shouldDoNothingWhenTagsEmpty() {
        tagService.attachTagsToPost(1L, List.of());
        verifyNoInteractions(tagRepository, postTagRepository);
    }

    @Test
    void shouldReplaceTags() {
        Long postId = 1L;
        List<String> tags = List.of("tag1");

        when(tagRepository.findByName("tag1")).thenReturn(null);
        when(tagRepository.save("tag1")).thenReturn(1L);

        tagService.replaceTags(postId, tags);

        verify(postTagRepository).deleteByPostId(postId);
        verify(postTagRepository).save(postId, 1L);
    }
}
