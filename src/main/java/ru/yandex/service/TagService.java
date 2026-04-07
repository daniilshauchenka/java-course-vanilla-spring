package ru.yandex.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.model.entity.TagEntity;
import ru.yandex.repository.PostTagRepository;
import ru.yandex.repository.TagRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    @Transactional
    public void attachTagsToPost(Long postId, List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return;
        }
            log.debug("Attaching tags to post id={}: {}", postId, tags);

        Set<String> uniqueTags = new HashSet<>(tags);
        for (String tagName : uniqueTags) {
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

    public Map<Long, List<String>> getTagsForPosts(List<Long> postIds) {
        return tagRepository.findTagsByPostIds(postIds);
    }

    public List<String> getTagsForPost(Long postId) {
        return getTagsForPosts(List.of(postId))
                .getOrDefault(postId, List.of());
    }

    @Transactional
    public void replaceTags(Long postId, List<String> tags) {
        log.debug("Replacing tags for post id={}", postId);
        postTagRepository.deleteByPostId(postId);
        attachTagsToPost(postId, tags);
    }
}