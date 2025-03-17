package hw6.integration.service.user;

import hw6.integration.controller.user.UserCreateRequestDto;
import hw6.integration.controller.user.UserUpdateNicknameRequestDto;
import hw6.integration.controller.user.UserUpdatePasswordRequestDto;
import hw6.integration.domain.model.User;

import java.util.List;

public interface UserService {

    List<User> getUserByAll();

    User getUserById(Long id);

    User registerUser(UserCreateRequestDto userCreateRequestDto);

    User updateNickname(Long id, UserUpdateNicknameRequestDto userUpdateNicknameRequestDto);

    User updatePassword(Long id, UserUpdatePasswordRequestDto userUpdatePasswordRequestDto);
}
