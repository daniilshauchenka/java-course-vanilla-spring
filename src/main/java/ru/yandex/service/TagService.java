package ru.yandex.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.mapper.CommentMapper;
import ru.yandex.model.dto.CommentCreateRequest;
import ru.yandex.model.dto.CommentDto;
import ru.yandex.model.entity.CommentEntity;
import ru.yandex.model.entity.TagEntity;
import ru.yandex.repository.CommentRepository;
import ru.yandex.repository.PostTagRepository;
import ru.yandex.repository.TagRepository;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    public void attachTagsToPost(Long postId, List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return;
        }
        for (String tagName : tags) {
            TagEntity existing = tagRepository.findByName(tagName);
            Long tagId;
            if (existing == null) {
                tagId = tagRepository.save(tagName);
            } else {
                tagId = existing.getId();
            }
            postTagRepository.save(postId, tagId);
        }
    }

    public List<String> getTagsForPost(Long postId) {
        return tagRepository.findTagsByPostId(postId);
    }

    public void replaceTags(Long postId, List<String> tags) {
        postTagRepository.deleteByPostId(postId);
        attachTagsToPost(postId, tags);
    }
}