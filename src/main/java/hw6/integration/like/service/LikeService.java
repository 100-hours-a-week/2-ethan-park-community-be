package hw6.integration.like.service;

public interface LikeService {

    void toggleLike(Long userId, Long postId);

    boolean isLiked(Long userId, Long postId);
}
