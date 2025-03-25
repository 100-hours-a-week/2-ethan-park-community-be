package hw6.integration.like.repository;

import hw6.integration.like.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeJpaRepository extends JpaRepository<LikeEntity, Long> {

    // 이미 좋아요한 상태인지 확인
    @Query(value = "SELECT COUNT(*) FROM likes WHERE user_id = :userId AND post_id = :postId AND like_active = true AND is_deleted = false", nativeQuery = true)
    int existsActiveLike(@Param("userId") Long userId, @Param("postId") Long postId);

    // 좋아요 추가 (새로 insert)
//    @Modifying
//    @Query(value = "INSERT INTO likes (user_id, post_id, like_active, is_deleted, created_at) VALUES (:userId, :postId, true, false, NOW())", nativeQuery = true)
//    void insertLike(@Param("userId") Long userId, @Param("postId") Long postId);

    // 기존 좋아요 비활성화 (소프트 딜리트 대체)
    @Modifying
    @Query(value = "UPDATE likes SET like_active = false, is_deleted = true WHERE user_id = :userId AND post_id = :postId", nativeQuery = true)
    void deactivateLike(@Param("userId") Long userId, @Param("postId") Long postId);
}
