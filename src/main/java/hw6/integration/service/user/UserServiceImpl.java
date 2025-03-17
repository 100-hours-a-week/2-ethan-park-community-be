package hw6.integration.service.user;

import hw6.integration.controller.user.UserCreateRequestDto;
import hw6.integration.controller.user.UserUpdateNicknameRequestDto;
import hw6.integration.controller.user.UserUpdatePasswordRequestDto;
import hw6.integration.domain.model.User;
import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.repository.user.UserRepository;
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
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    }

    @Override
    public User registerUser(UserCreateRequestDto userCreateRequestDto) {

        if(userRepository.existsByEmail(userCreateRequestDto.getEmail())){


        }

        return userRepository.save(userCreateRequestDto.toDomain());
    }

    @Override
    public User updateNickname(Long id,  UserUpdateNicknameRequestDto userUpdateNicknameRequestDto) {

        User userExisting = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        User updated = userExisting.updateNickname(userUpdateNicknameRequestDto.getNickname());

        return userRepository.save(updated);
    }

    @Override
    public User updatePassword(Long id, UserUpdatePasswordRequestDto userUpdatePasswordRequestDto) {

        User userExisting = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        User updated = userExisting.updatePassword(userUpdatePasswordRequestDto.getPassword());

        return userRepository.save(updated);
    }
}
