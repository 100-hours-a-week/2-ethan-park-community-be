package hw6.integration.user.service;

import hw6.integration.user.domain.User;
import hw6.integration.user.repository.UserReadRepository;
import hw6.integration.user.util.UserServiceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReadServiceImpl implements UserReadService {

    private final UserReadRepository userReadRepository;
    private final UserServiceValidator userServiceValidator;

    @Transactional(readOnly = true)
    @Override
    public List<User> getUserByAll() {

        return userReadRepository.findByAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(Long id) {

        return userServiceValidator.validateUserExists(id);

    }

}
