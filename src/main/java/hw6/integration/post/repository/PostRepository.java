package hw6.integration.post.repository;

import hw6.integration.post.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Optional<List<Post>> findByAll();

    Optional<Post> findById(Long id);
}
