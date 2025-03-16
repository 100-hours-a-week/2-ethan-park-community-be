package hw6.integration.service.user;

import hw6.integration.domain.model.User;
import hw6.integration.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUserByAll() {

        return userRepository.findByAll()
                .orElseThrow(() -> new EntityNotFoundException("유저가 없습니다."));
    }

    @Override
    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id + "번 유저가 없습니다."));

    }
}
