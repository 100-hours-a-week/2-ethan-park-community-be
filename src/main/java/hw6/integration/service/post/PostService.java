package hw6.integration.service.post;

import hw6.integration.domain.model.Post;

import java.util.List;

public interface PostService {

    List<Post> getPostByAll();

    Post getPostById(Long id);
}
