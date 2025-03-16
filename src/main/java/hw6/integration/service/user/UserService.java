package hw6.integration.service.user;

import hw6.integration.domain.model.User;

import java.util.List;

public interface UserService {

    List<User> getUserByAll();

    User getUserById(Long id);
}
