package hw6.integration.user.util;

import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class UserPasswordValidatorTest {

    private PasswordEncoder passwordEncoder;
    private UserPasswordValidator userPasswordValidator;

    @BeforeEach
    void setUp() {
        passwordEncoder = mock(PasswordEncoder.class);
        userPasswordValidator = new UserPasswordValidator(passwordEncoder);
    }

    @Test
    @DisplayName("newPassword와 beforePassword가 같으면 예외를 발생하지 않는다.")
    void should_return_nothing_when_newPassword_same_beforePassword() {

        String newPassword = "password";
        String beforePassword = "password";
        given(passwordEncoder.matches(newPassword, beforePassword)).willReturn(true);

        assertDoesNotThrow(() -> userPasswordValidator.validateUserPasswordSame(newPassword, beforePassword));
    }

    @Test
    @DisplayName("newPassword와 beforePassword가 다르면 예외를 발생한다.")
    void should_throw_exception_when_newPassword_not_same_beforePassword() {

        String newPassword = "newPassword";
        String beforePassword = "password";
        given(passwordEncoder.matches(newPassword, beforePassword)).willReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userPasswordValidator.validateUserPasswordSame(newPassword, beforePassword);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_PASSWORD);
    }
}