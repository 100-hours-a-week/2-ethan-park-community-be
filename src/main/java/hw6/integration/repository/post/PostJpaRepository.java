package hw6.integration.repository.post;

import hw6.integration.domain.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {

}
