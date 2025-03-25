package hw6.integration.user.service;

import hw6.integration.user.domain.User;

import java.util.List;

public interface UserReadService {

    List<User> getUserByAll();

    User getUserById(Long id);
}
