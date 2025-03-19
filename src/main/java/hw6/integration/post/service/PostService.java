package hw6.integration.post.service;

import hw6.integration.post.domain.Post;
import hw6.integration.post.dto.PostCreateDto;
import hw6.integration.post.dto.PostUpdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    List<Post> getPostByAll();

    Post getPostById(Long id);

    Post createPost(String title, String content, List<MultipartFile> images, Long id);

    Post updatePost(Long postId, PostUpdateDto postUpdateDto, Long userId);

    void deletePost(Long userId, Long postId);
}
