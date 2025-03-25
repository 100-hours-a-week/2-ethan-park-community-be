package hw6.integration.post.repository;

import hw6.integration.post.domain.Post;
import hw6.integration.post.entity.PostEntity;

import java.util.List;
import java.util.Optional;

public interface PostReadRepository {

    Optional<Post> findById(Long id);

    Optional<PostEntity> findEntityById(Long id);

    List<Post> findAllVisiblePosts();

}
