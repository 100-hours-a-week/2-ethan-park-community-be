package hw6.integration.repository.image;

import hw6.integration.domain.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageJpaRepository extends JpaRepository<ImageEntity, Long> {
}
