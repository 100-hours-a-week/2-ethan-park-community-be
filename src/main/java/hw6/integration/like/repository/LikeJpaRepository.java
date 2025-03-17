package hw6.integration.like.repository;

import hw6.integration.like.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeJpaRepository extends JpaRepository<LikeEntity, Long> {
}
