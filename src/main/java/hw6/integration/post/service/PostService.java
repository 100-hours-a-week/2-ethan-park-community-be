package hw6.integration.post.service;

import hw6.integration.post.domain.Post;

import java.util.List;

public interface PostService {

    List<Post> getPostByAll();

    Post getPostById(Long id);
}
