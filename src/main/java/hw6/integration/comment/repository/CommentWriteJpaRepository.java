package hw6.integration.comment.repository;

import hw6.integration.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface CommentWriteJpaRepository extends JpaRepository<CommentEntity, Long> {

    //native query
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE comments SET is_deleted = true WHERE post_id = :postId", nativeQuery = true)
    void deleteCommentByPostId(@Param("postId") Long postId);

    // 작성자명 업데이트
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE comments SET author_name = '알 수 없음' WHERE user_id = :userId", nativeQuery = true)
    void updateAuthorNameByUserId(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE comments SET is_deleted = true WHERE user_id = :userId", nativeQuery = true)
    void deleteCommentByUserId(@Param("userId") Long userId);


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
