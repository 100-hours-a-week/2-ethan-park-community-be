package hw6.integration.user.service;

import hw6.integration.config.JwtProvider;
import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

    @Test
    @DisplayName("사용자가 넘긴 이메일이 존재하지 않는다면 예외를 반환한다.")
    void should_throw_exception_email_when_email_is_not_exists() {

        //given
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto();
        userLoginRequestDto.setEmail("empty@naver.com");
        userLoginRequestDto.setPassword("password");
        given(userServiceValidator.validateUserEmailExists(userLoginRequestDto.getEmail())).willThrow(new BusinessException(ErrorCode.USER_NOT_FOUND));

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userAuthService.login(userLoginRequestDto);
        });

        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
        verify(userServiceValidator, times(1)).validateUserEmailExists(userLoginRequestDto.getEmail());
    }

    @Test
    @DisplayName("로그인 시도한 사용자가 활성 상태가 아닌 경우 예외를 반환한다.")
    void should_throw_exception_active_when_active_is_not() {

        //given
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto();
        userLoginRequestDto.setEmail("empty@naver.com");
        userLoginRequestDto.setPassword("password");
        User user = User.createUser(userLoginRequestDto.getEmail(), userLoginRequestDto.getPassword(), "test", "/profile");
        given(userServiceValidator.validateUserEmailExists(userLoginRequestDto.getEmail())).willReturn(user);

        doThrow(new BusinessException(ErrorCode.UNAUTHORIZED))
                .when(userEqualsValidator)
                .validateUserActive(user);

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userAuthService.login(userLoginRequestDto);
        });

        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.UNAUTHORIZED);
        verify(userServiceValidator, times(1)).validateUserEmailExists(userLoginRequestDto.getEmail());
        verify(userEqualsValidator, times(1)).validateUserActive(user);
    }

    @Test
    @DisplayName("입력한 비밀번호와 실제 저장된 비밀번호와 다른 경우 예외를 발생한다.")
    void should_throw_exception_password_when_password_is_not_same() {

        //given
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto();
        userLoginRequestDto.setEmail("empty@naver.com");
        userLoginRequestDto.setPassword("password");
        User user = User.createUser(userLoginRequestDto.getEmail(), userLoginRequestDto.getPassword(), "test", "/profile");
        given(userServiceValidator.validateUserEmailExists(userLoginRequestDto.getEmail())).willReturn(user);

        doThrow(new BusinessException(ErrorCode.INVALID_PASSWORD))
                .when(userPasswordValidator)
                .validateUserPasswordSame(userLoginRequestDto.getPassword(), user.getPassword());

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userAuthService.login(userLoginRequestDto);
        });

        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_PASSWORD);
        verify(userServiceValidator, times(1)).validateUserEmailExists(userLoginRequestDto.getEmail());
        verify(userEqualsValidator, times(1)).validateUserActive(user);
        verify(userPasswordValidator, times(1)).validateUserPasswordSame(userLoginRequestDto.getPassword(), user.getPassword());
    }

}