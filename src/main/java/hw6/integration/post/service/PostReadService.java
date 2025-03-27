package hw6.integration.post.service;

import hw6.integration.post.domain.Post;

import java.util.List;

public interface PostReadService {

    List<Post> getPostByAll();

    Post findById(Long id);
}
