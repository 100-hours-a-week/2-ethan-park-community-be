package hw6.integration.user.service;

import hw6.integration.user.dto.UserLoginRequestDto;
import hw6.integration.user.dto.UserSignupRequestDto;
import hw6.integration.user.dto.UserUpdateNicknameRequestDto;
import hw6.integration.user.dto.UserUpdatePasswordRequestDto;
import hw6.integration.user.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<User> getUserByAll();

    User getUserById(Long id);

    User registerUser(UserSignupRequestDto userSignupRequestDto);

    String login(UserLoginRequestDto userLoginRequestDto);


    User updateNickname(Long id, UserUpdateNicknameRequestDto userUpdateNicknameRequestDto);

    void updatePassword(Long id, UserUpdatePasswordRequestDto userUpdatePasswordRequestDto);

    void deleteUser(Long id);
}
