package hw6.integration.post.repository;

import hw6.integration.post.domain.Post;
import hw6.integration.post.entity.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostReadRepositoryImpl implements PostReadRepository {

    private final PostReadJpaRepository postReadJpaRepository;

    @Override
    public Optional<Post> findById(Long id) {
        return postReadJpaRepository.findById(id).map(PostEntity::toDomain);
    }

    @Override
    public List<Post> findAllVisiblePosts() {
        return postReadJpaRepository.findByIsDeletedFalse()
                .stream()
                .map(PostEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<PostEntity> findEntityById(Long id) {
        return postReadJpaRepository.findById(id);
    }
}
