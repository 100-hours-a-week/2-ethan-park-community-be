package hw6.integration.like.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;

    @Override
    public int existsActiveLike(Long userId, Long postId) {
        return likeJpaRepository.existsActiveLike(userId, postId);
    }

    @Override
    public void insertLike(Long userId, Long postId) {
        likeJpaRepository.insertLike(userId, postId);
    }

    @Override
    public void deactivateLike(Long userId, Long postId) {
        likeJpaRepository.deactivateLike(userId, postId);
    }
}
