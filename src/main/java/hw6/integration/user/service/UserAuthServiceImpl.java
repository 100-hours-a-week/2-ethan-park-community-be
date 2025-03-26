package hw6.integration.user.service;

import hw6.integration.config.JwtProvider;
import hw6.integration.user.domain.User;
import hw6.integration.user.dto.UserLoginRequestDto;
import hw6.integration.user.util.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final JwtProvider jwtProvider;
    private final UserValidator userValidator;

    @Transactional
    @Override
    public String login(UserLoginRequestDto userLoginRequestDto) {
        User user = userValidator.validateUserEmailExists(userLoginRequestDto.getEmail());

        userValidator.validateUserActive(user);

        userValidator.validateUserPasswordSame(userLoginRequestDto.getPassword(), user.getPassword());

        return jwtProvider.generateToken(user);
    }
}
