package hw6.integration.user.repository;

import hw6.integration.user.domain.User;

public interface UserWriterRepository {

    User save(User user);

}
