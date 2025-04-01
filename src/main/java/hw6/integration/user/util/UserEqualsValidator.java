package hw6.integration.user.util;


import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEqualsValidator {

    public void validateUserActive(User user) {
        if (!user.getIsActive())
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }

    public void validateUserAndPostEquals(Long userId, Long post_userId) {

        if (!userId.equals(post_userId))
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }

    public void validateUserAndCommentEntityEquals(Long userId, Long comment_userId) {

        if (!userId.equals(comment_userId))
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }
}

