package hw6.integration.user.util;

import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.user.domain.User;
import hw6.integration.user.repository.UserReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserReadRepository userReadRepository;
    private final PasswordEncoder passwordEncoder;


    public User validateUserExists(Long userId) {
        return userReadRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    public void validateUserActive(User user) {
        if (!user.getIsActive())
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }

    public User validateUserEmailExists(String email) {
        // 이메일이 존재하지 않으면 예외를 던짐
        return userReadRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND)); // 이메일이 없으면 예외 던짐
    }

    public void validateUserEmailDuplicate(String email) {
        // 이메일이 존재하면 예외를 던짐
        userReadRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new BusinessException(ErrorCode.EMAIL_DUPLICATE); // 이미 존재하면 예외 던짐
                });
    }

    public void validateUserNicknameDuplicate(String nickname) {
        userReadRepository.findByNickname(nickname)
                .ifPresent(user -> new BusinessException(ErrorCode.NICKNAME_DUPLICATE));
    }

    public void validateUserPasswordSame(String newPassword, String beforePassword) {
        System.out.println(newPassword + " !!! " + beforePassword);
        if (!passwordEncoder.matches(newPassword, beforePassword))
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
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
