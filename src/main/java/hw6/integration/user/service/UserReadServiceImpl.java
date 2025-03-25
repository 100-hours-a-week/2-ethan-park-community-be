package hw6.integration.user.service;

import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.user.domain.User;
import hw6.integration.user.repository.UserReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReadServiceImpl implements UserReadService {

    private final UserReadRepository userReadRepository;

    @Override
    public List<User> getUserByAll() {

        return userReadRepository.findByAll();
    }

    @Override
    public User getUserById(Long id) {

        return userReadRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    }

}
