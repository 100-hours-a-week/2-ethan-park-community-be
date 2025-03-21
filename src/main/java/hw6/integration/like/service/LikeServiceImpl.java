package hw6.integration.like.service;

import hw6.integration.like.repository.LikeRepository;
import hw6.integration.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeJpaRepository;
    private final PostRepository postRepository;

    @Transactional
    @Override
    public void toggleLike(Long userId, Long postId) {
        int exists = likeJpaRepository.existsActiveLike(userId, postId);

        if (exists > 0) {
            likeJpaRepository.deactivateLike(userId, postId);  // 좋아요 취소
            postRepository.decrementLikeCount(postId);
        } else {
            likeJpaRepository.insertLike(userId, postId);      // 좋아요 등록
            postRepository.incrementLikeCount(postId);
        }
    }
}
