package hw6.integration.user.service;

import hw6.integration.user.domain.User;
import hw6.integration.user.dto.UserSignupRequestDto;
import hw6.integration.user.dto.UserUpdateNicknameRequestDto;
import hw6.integration.user.dto.UserUpdatePasswordRequestDto;

public interface UserWriterService {

    User registerUser(UserSignupRequestDto userSignupRequestDto);

    User updateNickname(Long id, UserUpdateNicknameRequestDto userUpdateNicknameRequestDto);

    void updatePassword(Long id, UserUpdatePasswordRequestDto userUpdatePasswordRequestDto);

    void deleteUser(Long id);
}
