package hw6.integration.user.service;

import hw6.integration.user.domain.User;
import hw6.integration.user.repository.UserReadRepository;
import hw6.integration.user.util.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReadServiceImpl implements UserReadService {

    private final UserReadRepository userReadRepository;
    private final UserValidator userValidator;

    @Override
    public List<User> getUserByAll() {

        return userReadRepository.findByAll();
    }

    @Override
    public User getUserById(Long id) {

        return userValidator.validateUserExists(id);

    }

}
