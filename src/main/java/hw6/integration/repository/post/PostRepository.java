package hw6.integration.repository.post;

import hw6.integration.domain.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Optional<List<Post>> findByAll();

    Optional<Post> findById(Long id);
}
