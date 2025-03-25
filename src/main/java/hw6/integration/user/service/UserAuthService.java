package hw6.integration.user.service;

import hw6.integration.user.dto.UserLoginRequestDto;

public interface UserAuthService {

    String login(UserLoginRequestDto userLoginRequestDto);

}
