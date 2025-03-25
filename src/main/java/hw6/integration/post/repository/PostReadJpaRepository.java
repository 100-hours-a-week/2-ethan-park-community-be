package hw6.integration.post.repository;

import hw6.integration.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostReadJpaRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findByUserEntity_Id(Long userId);

    //native query

    @Query(value = "SELECT * FROM posts WHERE is_deleted = false", nativeQuery = true)
    List<PostEntity> findByIsDeletedFalse();

    // JPQL
    //    @Query("SELECT p FROM PostEntity p WHERE p.isDeleted = false")
//    List<PostEntity> findByIsDeletedFalse();
}
