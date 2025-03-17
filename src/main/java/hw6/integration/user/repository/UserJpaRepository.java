package hw6.integration.user.repository;

import hw6.integration.user.domain.User;
import hw6.integration.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<User> findByEmail(String email);
}
