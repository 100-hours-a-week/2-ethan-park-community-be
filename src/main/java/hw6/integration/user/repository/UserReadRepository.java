package hw6.integration.user.repository;

import hw6.integration.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserReadRepository {

    List<User> findByAll();

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

}
