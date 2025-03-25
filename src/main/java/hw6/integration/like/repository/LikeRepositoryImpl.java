package hw6.integration.like.repository;

import hw6.integration.like.domain.Like;
import hw6.integration.post.domain.Post;
import hw6.integration.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;

    @Override
    public int existsActiveLike(Long userId, Long postId) {
        return likeJpaRepository.existsActiveLike(userId, postId);
    }

    @Transactional
    @Override
    public Like save(Like like, User user, Post post) {

        return likeJpaRepository.save(Like.toEntity(like, Post.toEntity(post, User.toEntity(user)), User.toEntity(user))).toDomain();

    }

    @Override
    public void deactivateLike(Long userId, Long postId) {
        likeJpaRepository.deactivateLike(userId, postId);
    }
}
