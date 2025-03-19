package hw6.integration.post.repository;

import hw6.integration.post.domain.Post;
import hw6.integration.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findByUserEntity_Id(Long userId);
}
