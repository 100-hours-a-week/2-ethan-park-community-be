package hw6.integration.comment.repository;

import hw6.integration.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentReadJpaRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findByUserEntity_Id(Long userId);

    @Query(value = "SELECT * FROM comments WHERE post_id = :postId and is_deleted = false", nativeQuery = true)
    List<CommentEntity> findByIsDeletedFalse(@Param("postId") Long postId);

    // 간단한 조회이므로 JPQL 사용(네트워크 비용 + 메모리 사용량 감소)
//    @Query("SELECT c FROM CommentEntity c WHERE c.isDeleted = false")
//    List<CommentEntity> findByIsDeletedFalse();
}
