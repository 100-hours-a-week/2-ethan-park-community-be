package hw6.integration.comment.repository;

import hw6.integration.comment.entity.CommentEntity;
import hw6.integration.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findByUserEntity_Id(Long userId);

    //native query
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE comments SET is_deleted = true WHERE post_id = :postId", nativeQuery = true)
    void deleteCommentByPostId(@Param("postId") Long postId);

    @Query(value = "SELECT * FROM comments WHERE post_id = :postId and is_deleted = false", nativeQuery = true)
    List<CommentEntity> findByIsDeletedFalse(@Param("postId") Long postId);

    // 작성자명 업데이트
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE comments SET author_name = '알 수 없음' WHERE user_id = :userId", nativeQuery = true)
    void updateAuthorNameByUserId(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE comments SET is_deleted = false WHERE user_id = :userId", nativeQuery = true)
    void deleteCommentByUserId(@Param("userId") Long userId);

    // 간단한 조회이므로 JPQL 사용(네트워크 비용 + 메모리 사용량 감소)
//    @Query("SELECT c FROM CommentEntity c WHERE c.isDeleted = false")
//    List<CommentEntity> findByIsDeletedFalse();
//
//    // 작성자명 업데이트
//    @Modifying(clearAutomatically = true)
//    @Transactional
//    @Query("UPDATE CommentEntity c SET c.authorName = :authorName WHERE c.userEntity.id = :userId")
//    void updateAuthorNameByUserId(Long userId, String authorName);
//
//    @Modifying(clearAutomatically = true)
//    @Transactional
//    @Query("UPDATE CommentEntity c SET c.isDeleted = :isDeleted WHERE c.userEntity.id = :userId")
//    void deleteCommentByUserId(Long userId, boolean isDeleted);
//
//    @Modifying(clearAutomatically = true)
//    @Transactional
//    @Query("UPDATE CommentEntity c SET c.isDeleted = :isDeleted WHERE c.postEntity.id = :postId")
//    void deleteCommentByPostId(Long postId, boolean isDeleted);


}
