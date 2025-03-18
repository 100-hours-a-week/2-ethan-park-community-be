package hw6.integration.post.repository;

import hw6.integration.post.domain.Post;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Optional<List<Post>> findByAll();

    Optional<Post> findById(Long id);

    Post save(Post post, UserEntity userEntity);

    Optional<PostEntity> findEntityById(Long id);

}
