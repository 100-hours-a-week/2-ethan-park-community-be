package hw6.integration.repository.like;

import hw6.integration.domain.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeJpaRepository extends JpaRepository<LikeEntity, Long> {
}
