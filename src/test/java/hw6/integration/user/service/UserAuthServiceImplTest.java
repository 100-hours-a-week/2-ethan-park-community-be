package hw6.integration.user.service;

import hw6.integration.config.JwtProvider;
import hw6.integration.user.domain.User;
import hw6.integration.user.dto.UserLoginRequestDto;
import hw6.integration.user.util.UserEqualsValidator;
import hw6.integration.user.util.UserPasswordValidator;
import hw6.integration.user.util.UserServiceValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceImplTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserServiceValidator userServiceValidator;

    @Mock
    private UserEqualsValidator userEqualsValidator;

    @Mock
    private UserPasswordValidator userPasswordValidator;

    @InjectMocks
    private UserAuthServiceImpl userAuthService;

    @Test
    @DisplayName("로그인 정보가 들어오면 login에 성공한다.")
    void should_success_login() {

        //given
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto();
        userLoginRequestDto.setEmail("test@naver.com");
        userLoginRequestDto.setPassword("password");
        User user = User.createUser(userLoginRequestDto.getEmail(), userLoginRequestDto.getPassword(), "test", "/profile");
        String token = "token";
        given(userServiceValidator.validateUserEmailExists(userLoginRequestDto.getEmail())).willReturn(user);
        given(jwtProvider.generateToken(user)).willReturn(token);

        //when
        String result = userAuthService.login(userLoginRequestDto);

        //then
        assertThat(result).isEqualTo(token);
        verify(userServiceValidator, times(1)).validateUserEmailExists(userLoginRequestDto.getEmail());
        verify(userEqualsValidator, times(1)).validateUserActive(user);
        verify(userPasswordValidator, times(1)).validateUserPasswordSame(userLoginRequestDto.getPassword(), user.getPassword());
        verify(jwtProvider, times(1)).generateToken(user);
    }

}