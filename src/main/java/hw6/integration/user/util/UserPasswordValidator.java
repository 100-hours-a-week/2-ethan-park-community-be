package hw6.integration.user.util;

import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPasswordValidator {
    private final PasswordEncoder passwordEncoder;

    public void validateUserPasswordSame(String newPassword, String beforePassword) {
        System.out.println(newPassword + " !!! " + beforePassword);
        if (!passwordEncoder.matches(newPassword, beforePassword))
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
    }

}
