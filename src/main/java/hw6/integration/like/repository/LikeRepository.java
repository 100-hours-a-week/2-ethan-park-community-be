package hw6.integration.like.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository {

    int existsActiveLike(Long userId, Long postId);

    void insertLike(Long userId, Long postId);

    // 기존 좋아요 비활성화 (소프트 딜리트 대체)
    void deactivateLike(Long userId, Long postId);
}
