package hw6.integration.user.util;

import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.user.domain.User;
import hw6.integration.user.repository.UserReadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class UserServiceValidatorTest {

    private UserReadRepository userReadRepository;
    private UserServiceValidator userServiceValidator;

    @BeforeEach
    void setUp() {
        userReadRepository = mock(UserReadRepository.class);
        userServiceValidator = new UserServiceValidator(userReadRepository);
    }

    @Test
    @DisplayName("사용자가 존재하면 User 반환")
    void should_return_user_when_user_exists() {

        //given
        Long userId = 1L;
        User user = mock(User.class);
        given(userReadRepository.findById(userId)).willReturn(Optional.of(user));

        //when
        User result = userServiceValidator.validateUserExists(userId);

        //then
        assertThat(result).isEqualTo(user);
        verify(userReadRepository, times(1)).findById(userId);

    }

    @Test
    @DisplayName("사용자가 존재하지 않으면 예외 발생")
    void should_throw_exception_when_user_is_not_exists() {

        //given
        Long userId = 999L;
        given(userReadRepository.findById(userId)).willReturn(Optional.empty());

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userServiceValidator.validateUserExists(userId);
        });

        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
        verify(userReadRepository, times(1)).findById(userId);

    }

    @Test
    @DisplayName("사용자 이메일이 존재하면 해당 User를 리턴")
    void should_return_user_when_email_exists() {

        //given
        String email = "test@naver.com";
        User user = mock(User.class);
        given(userReadRepository.findByEmail(email)).willReturn(Optional.of(user));

        //when
        User result = userServiceValidator.validateUserEmailExists(email);

        //then
        assertThat(result).isEqualTo(user);
        verify(userReadRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("사용자 이메일이 존재하지 않으면 예외를 던진다")
    void should_throw_exception_when_email_is_not_exists() {

        //given
        String email = "test@naver.com";
        given(userReadRepository.findByEmail(email)).willReturn(Optional.empty());

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userServiceValidator.validateUserEmailExists(email);
        });

        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
        verify(userReadRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("사용자 이메일이 중복되지 않으면 예외를 던지면 안된다.")
    void should_return_nothing_when_email_is_not_duplicated() {

        //given
        String email = "test@naver.com";
        given(userReadRepository.findByEmail(email)).willReturn(Optional.empty());

        //when & then
        assertDoesNotThrow(() -> userServiceValidator.validateUserEmailDuplicate(email));
        verify(userReadRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("사용자 이메일이 중복되면 예외를 던진다.")
    void should_throw_exception_when_email_is_duplicated() {

        //given
        String email = "test@naver.com";
        User duplicatedUser = mock(User.class);
        given(userReadRepository.findByEmail(email)).willReturn(Optional.of(duplicatedUser));

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userServiceValidator.validateUserEmailDuplicate(email);
        });

        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.EMAIL_DUPLICATE);
        verify(userReadRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("사용자 닉네임이 중복되지 않으면 예외를 던지지 않는다.")
    void should_return_nothing_when_nickname_is_not_duplicated() {

        //given
        String nickname = "test";
        given(userReadRepository.findByNickname(nickname)).willReturn(Optional.empty());

        //when & then
        assertDoesNotThrow(() -> userServiceValidator.validateUserNicknameDuplicate(nickname));
        verify(userReadRepository, times(1)).findByNickname(nickname);
    }

    @Test
    @DisplayName("사용자 닉네임이 중복되면 예외를 던진다.")
    void should_throw_exception_when_nickname_is_duplicated() {

        //given
        String nickname = "test";
        User duplicatedUser = mock(User.class);
        given(userReadRepository.findByNickname(nickname)).willReturn(Optional.of(duplicatedUser));

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userServiceValidator.validateUserNicknameDuplicate(nickname);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NICKNAME_DUPLICATE);
        verify(userReadRepository, times(1)).findByNickname(nickname);

    }


}