package hw6.integration.post.service;

import hw6.integration.post.domain.Post;
import hw6.integration.post.dto.PostCreateRequestDto;
import hw6.integration.post.dto.PostUpdateRequestDto;

import java.util.List;

public interface PostService {

    List<Post> getPostByAll();

    Post getPostById(Long id);

    Post createPost(PostCreateRequestDto postCreateRequestDto, Long id);

    Post updatePost(Long postId, PostUpdateRequestDto postUpdateRequestDto, Long userId);

    void deletePost(Long userId, Long postId);
}
