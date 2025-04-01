package hw6.integration.user.service;

import hw6.integration.config.JwtProvider;
import hw6.integration.user.domain.User;
import hw6.integration.user.dto.UserLoginRequestDto;
import hw6.integration.user.util.UserEqualsValidator;
import hw6.integration.user.util.UserPasswordValidator;
import hw6.integration.user.util.UserServiceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final JwtProvider jwtProvider;
    private final UserServiceValidator userServiceValidator;
    private final UserEqualsValidator userEqualsValidator;
    private final UserPasswordValidator userPasswordValidator;

    @Transactional
    @Override
    public String login(UserLoginRequestDto userLoginRequestDto) {
        User user = userServiceValidator.validateUserEmailExists(userLoginRequestDto.getEmail());

        userEqualsValidator.validateUserActive(user);

        userPasswordValidator.validateUserPasswordSame(userLoginRequestDto.getPassword(), user.getPassword());

        return jwtProvider.generateToken(user);
    }
}
