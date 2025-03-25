package hw6.integration.post.service;

import hw6.integration.post.domain.Post;
import hw6.integration.post.dto.PostCreateRequestDto;
import hw6.integration.post.dto.PostUpdateRequestDto;

public interface PostWriterService {

    Post createPost(PostCreateRequestDto postCreateRequestDto, Long id);

    Post updatePost(Long postId, PostUpdateRequestDto postUpdateRequestDto, Long userId);

    void deletePost(Long userId, Long postId);
}
