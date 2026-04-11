package ru.yandex.springbootblog.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.exception.ExceptionType;
import ru.yandex.exception.MyException;
import ru.yandex.mapper.PostMapper;
import ru.yandex.model.dto.ImageDto;
import ru.yandex.model.dto.PostCreateRequest;
import ru.yandex.model.dto.PostDto;
import ru.yandex.model.dto.PostPageResponse;
import ru.yandex.model.dto.PostUpdateRequest;
import ru.yandex.model.entity.PostEntity;
import ru.yandex.repository.CommentRepository;
import ru.yandex.repository.PostRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TagService tagService;

    public PostPageResponse getPosts(String search, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new MyException(ExceptionType.INVALID_PAGINATION, pageNumber, pageSize);
        }
        log.debug("Fetching posts: search='{}', page={}, size={}", search, pageNumber, pageSize);

        int total = postRepository.countPosts(search);
        int offset = (pageNumber - 1) * pageSize;

        List<PostEntity> posts = postRepository.findPosts(search, offset, pageSize);

        List<Long> ids = posts.stream()
            .map(PostEntity::getId)
            .toList();

        Map<Long, List<String>> tagsMap = tagService.getTagsForPosts(ids);

        List<PostDto> dtos = posts.stream()
            .map(post -> PostMapper.toDto(
                post,
                tagsMap.getOrDefault(post.getId(), List.of())
            ))
            .map(this::truncateText)
            .toList();

        int lastPage = (int) Math.ceil((double) total / pageSize);

        return PostPageResponse.builder()
            .posts(dtos)
            .hasPrev(pageNumber > 1)
            .hasNext(pageNumber < lastPage)
            .lastPage(lastPage)
            .build();
    }

    public PostDto getPostById(Long id) {
        log.debug("Fetching post by id={}", id);
        PostEntity post = getPostOrThrow(id);

        List<String> tags = tagService.getTagsForPost(id);
        return PostMapper.toDto(post, tags);
    }

    @Transactional
    public PostDto createPost(PostCreateRequest request) {
        log.info("Creating post: title='{}'", request.getTitle());
        PostEntity post = PostMapper.toEntityFromCreateRequest(request);
        Long id = postRepository.save(post);
        tagService.attachTagsToPost(id, request.getTags());
        PostEntity saved = postRepository.findById(id);
        List<String> tags = tagService.getTagsForPost(id);
        log.info("Post created: id={}", id);
        return PostMapper.toDto(saved, tags);
    }

    @Transactional
    public void deletePost(Long id) {
        log.info("Deleting post id={}", id);
        getPostOrThrow(id);
        commentRepository.deleteByPostId(id);
        postRepository.deleteById(id);
        log.info("Post deleted id={}", id);
    }

    @Transactional
    public Long incrementLikes(Long id) {
        log.debug("Incrementing likes for post id={}", id);
        getPostOrThrow(id);
        return postRepository.incrementLikes(id);
    }

    private PostDto truncateText(PostDto dto) {
        String text = dto.getText();
        if (text != null && text.length() > 128) {
            text = text.substring(0, 128) + "...";
        }
        dto.setText(text);
        return dto;
    }

    @Transactional
    public PostDto updatePost(Long id, PostUpdateRequest request) {
        log.info("Updating post id={}", id);
        PostEntity existing = getPostOrThrow(id);
        PostEntity updated = PostEntity.builder()
            .id(id)
            .title(request.getTitle() != null ? request.getTitle() : existing.getTitle())
            .text(request.getText() != null ? request.getText() : existing.getText())
            .likesCount(existing.getLikesCount())
            .commentsCount(existing.getCommentsCount())
            .build();

        postRepository.update(updated);
        tagService.replaceTags(id, request.getTags());
        PostEntity saved = postRepository.findById(id);
        List<String> tags = tagService.getTagsForPost(id);
        log.info("Post updated id={}", id);
        return PostMapper.toDto(saved, tags);
    }


    @Transactional
    public void uploadImage(Long postId, MultipartFile file) {
        log.info("Uploading image for post id={}", postId);
        getPostOrThrow(postId);
        try {
            if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                throw new MyException(ExceptionType.INVALID_REQUEST, "Only images allowed");
            }
            byte[] bytes = file.getBytes();
            String contentType = file.getContentType();
            postRepository.updateImage(postId, bytes, contentType);
        } catch (IOException e) {
            log.error("Failed to upload image", e);
            throw new MyException(ExceptionType.INVALID_REQUEST, "Invalid image file");
        }
    }

    public ImageDto getImage(Long postId) {
        log.debug("Fetching image for post id={}", postId);
        getPostOrThrow(postId);
        ImageDto image = postRepository.getImage(postId);
        if (image == null || image.getData() == null) {
            throw new MyException(ExceptionType.IMAGE_NOT_FOUND, postId);
        }
        return image;
    }

    private PostEntity getPostOrThrow(Long id) {
        PostEntity post = postRepository.findById(id);
        if (post == null) {
            throw new MyException(ExceptionType.POST_NOT_FOUND, id);
        }
        return post;
    }
}
