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
    public void updateAuthorName(Long userId, String newNickname) {
        List<PostEntity> posts = postJpaRepository.findByUserEntity_Id(userId);
        for (PostEntity post : posts) {
            post.setAuthorName(newNickname); // ✅ 변경 감지
        }
    }


    @Override
    public List<Post> findAllVisiblePosts() {
        return postJpaRepository.findByIsDeletedFalse()
                        .stream()
                        .map(PostEntity::toDomain)
                        .toList();
    }

    @Override
    public Optional<PostEntity> findEntityById(Long id) {
        return postJpaRepository.findById(id);
    }

    @Override
    public void incrementViewCount(Long id) {
        postJpaRepository.incrementViewCount(id);
    }

    @Override
    public void incrementContentCount(Long id) {
        postJpaRepository.incrementContentCount(id);
    }

    @Override
    public void decrementContentCount(Long id) {
        postJpaRepository.decrementContentCount(id);
    }

    @Override
    public void incrementLikeCount(Long id) {
        postJpaRepository.incrementLikeCount(id);
    }

    @Override
    public void decrementLikeCount(Long id) {
        postJpaRepository.decrementLikeCount(id);
    }

    @Override
    public void deletePostByUserId(Long userId, boolean delete, String deletedUser) {
        postJpaRepository.deletePostByUserId(userId, delete);
        postJpaRepository.updateAuthorNameByUserId(userId, deletedUser);
    }
}
