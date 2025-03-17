package hw6.integration.post.repository;

import hw6.integration.post.entity.PostEntity;
import hw6.integration.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;


    @Override
    public Optional<List<Post>> findByAll() {
        return Optional.of(
                postJpaRepository.findAll().stream()
                        .map(PostEntity::toDomain)
                        .toList()
        );
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postJpaRepository.findById(id).map(PostEntity::toDomain);
    }
}
