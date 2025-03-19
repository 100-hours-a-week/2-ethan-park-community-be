package hw6.integration.post.repository;

import hw6.integration.post.entity.PostEntity;
import hw6.integration.post.domain.Post;
import hw6.integration.user.entity.UserEntity;
import hw6.integration.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public Post save(Post post, UserEntity userEntity) {

        PostEntity postEntity = Post.toEntity(post, userEntity);
        PostEntity saved = postJpaRepository.save(postEntity);

        return saved.toDomain();
    }

    @Override
    public List<Post> saveAll(List<Post> posts, UserEntity userEntity) {

        List<PostEntity> entities = posts.stream()
                .map(post -> Post.toEntity(post, userEntity))
                .toList();

        List<PostEntity> saved = postJpaRepository.saveAll(entities);

        return saved.stream()
                .map(PostEntity::toDomain)
                .toList();
    }

    @Override
    public void delete(Long postId) {

        postJpaRepository.deleteById(postId);
    }

    @Override
    public Optional<PostEntity> findEntityById(Long id) {
        return postJpaRepository.findById(id);
    }

    @Override
    public List<Post> findByUserId(Long userId) {
        return postJpaRepository.findByUserEntity_Id(userId)
                .stream()
                .map(PostEntity::toDomain)
                .toList();
    }

}
