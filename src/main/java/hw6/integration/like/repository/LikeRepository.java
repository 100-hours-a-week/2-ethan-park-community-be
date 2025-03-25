package hw6.integration.like.repository;


import hw6.integration.like.domain.Like;
import hw6.integration.post.domain.Post;
import hw6.integration.user.domain.User;

public interface LikeRepository {

    int existsActiveLike(Long userId, Long postId);

    Like save(Like like, User user, Post post);

    // 기존 좋아요 비활성화 (소프트 딜리트 대체)
    void deactivateLike(Long userId, Long postId);
}
