package hw6.integration.user.util;

import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


class UserEqualsValidatorTest {

    private UserEqualsValidator userEqualsValidator;

    @BeforeEach
    void setUp() {
        userEqualsValidator = new UserEqualsValidator();
    }


    @Test
    @DisplayName("사용자가 활성화 상태라면 예외가 발생하면 안된다.")
    void should_return_nothing_when_user_is_active() {

        //given
        User user = mock(User.class);
        given(user.getIsActive()).willReturn(true);

        assertDoesNotThrow(() -> userEqualsValidator.validateUserActive(user));
    }

    @Test
    @DisplayName("사용자가 활성화 상태라면 예외가 발생하면 안된다.")
    void should_throw_exception_when_user_is_not_active() {

        //given
        User user = mock(User.class);
        given(user.getIsActive()).willReturn(false);

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userEqualsValidator.validateUserActive(user);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.UNAUTHORIZED);
    }

    @Test
    @DisplayName("userId와 postId가 같으면 예외가 발생하지 않는다.")
    void should_return_nothing_when_userId_same_postId() {

        //given
        Long userId = 1L;
        Long postId = 1L;

        assertDoesNotThrow(() -> userEqualsValidator.validateUserAndPostEquals(userId, postId));
    }

    @Test
    @DisplayName("userId와 postId가 다르면 예외가 발생한다.")
    void should_throw_exception_when_userId_not_same_postId() {

        //given
        Long userId = 1L;
        Long postId = 2L;

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userEqualsValidator.validateUserAndPostEquals(userId, postId);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.UNAUTHORIZED);

    }

    @Test
    @DisplayName("userId와 comment_userId가 같으면 예외가 발생하지 않는다.")
    void should_return_nothing_when_userId_same_comment_userId() {

        //given
        Long userId = 1L;
        Long comment_userId = 1L;

        assertDoesNotThrow(() -> userEqualsValidator.validateUserAndPostEquals(userId, comment_userId));
    }

    @Test
    @DisplayName("userId와 postId가 다르면 예외가 발생한다.")
    void should_throw_exception_when_userId_not_same_comment_userId() {

        //given
        Long userId = 1L;
        Long comment_userId = 2L;

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userEqualsValidator.validateUserAndPostEquals(userId, comment_userId);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.UNAUTHORIZED);

    }


}