package hw6.integration.post.repository;

import hw6.integration.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostWriteJpaRepository extends JpaRepository<PostEntity, Long> {

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE posts SET author_name = '알 수 없음' WHERE user_id = :userId", nativeQuery = true)
    void updateAuthorNameByUserId(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE posts SET like_count = like_count + 1 WHERE id = :postId", nativeQuery = true)
    void incrementLikeCount(@Param("postId") Long postId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE posts SET like_count = like_count - 1 WHERE id = :postId", nativeQuery = true)
    void decrementLikeCount(@Param("postId") Long postId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE posts SET is_deleted = true WHERE user_id = :userId", nativeQuery = true)
    void deletePostByUserId(@Param("userId") Long userId);


//    @Modifying(clearAutomatically = true)
//    @Transactional
//    @Query("UPDATE PostEntity p SET p.authorName = :authorName WHERE p.userEntity.id = :userId")
//    void updateAuthorNameByUserId(Long userId, String authorName);
//
//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE PostEntity p SET p.view_count = p.view_count + 1 WHERE p.id = :postId")
//    void incrementViewCount(Long postId);
//
//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE PostEntity p SET p.comment_count = p.comment_count + 1 WHERE p.id = :postId")
//    void incrementContentCount(Long postId);
//
//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE PostEntity p SET p.comment_count = p.comment_count - 1 WHERE p.id = :postId")
//    void decrementContentCount(Long postId);
//
//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE PostEntity p SET p.like_count = p.like_count + 1 WHERE p.id = :postId")
//    void incrementLikeCount(Long postId);
//
//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE PostEntity p SET p.like_count = p.like_count - 1 WHERE p.id = :postId")
//    void decrementLikeCount(Long postId);
//
//    @Modifying(clearAutomatically = true)
//    @Transactional
//    @Query("UPDATE PostEntity p SET p.isDeleted = :isDeleted WHERE p.userEntity.id = :userId")
//    void deletePostByUserId(Long userId, boolean isDeleted);
}
