package hw6.integration.repository.user;

import hw6.integration.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<List<User>> findByAll();

    Optional<User> findById(Long id);

    boolean existsByEmail(String email);

    User save(User user);
}
