package hw6.integration.user.repository;

import hw6.integration.post.domain.Post;
import hw6.integration.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findByAll();

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    User save(User user);
}
