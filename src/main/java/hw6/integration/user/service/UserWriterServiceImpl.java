package hw6.integration.user.service;

import hw6.integration.comment.repository.CommentWriteRepository;
import hw6.integration.image.component.ImageComponent;
import hw6.integration.post.repository.PostWriteRepository;
import hw6.integration.user.domain.User;
import hw6.integration.user.dto.UserSignupRequestDto;
import hw6.integration.user.dto.UserUpdateNicknameRequestDto;
import hw6.integration.user.dto.UserUpdatePasswordRequestDto;
import hw6.integration.user.repository.UserWriterRepository;
import hw6.integration.user.util.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserWriterServiceImpl implements UserWriterService {

    private final UserWriterRepository userWriterRepository;
    private final ImageComponent imageComponent;
    private final PasswordEncoder passwordEncoder;
    private final PostWriteRepository postWriteRepository;
    private final CommentWriteRepository commentWriteRepository;

    private final UserValidator userValidator;

    @Transactional
    @Override
    public User registerUser(UserSignupRequestDto userSignupRequestDto) {

        userValidator.validateUserEmailDuplicate(userSignupRequestDto.getEmail());

        userValidator.validateUserNicknameDuplicate(userSignupRequestDto.getNickname());

        String profilePath = null;
        if (userSignupRequestDto.getProfileImage() != null && !userSignupRequestDto.getProfileImage().isEmpty()) {
            profilePath = imageComponent.uploadProfileImage(userSignupRequestDto.getProfileImage());
        }

        User user = User.createUser(
                userSignupRequestDto.getEmail(),
                passwordEncoder.encode(userSignupRequestDto.getPassword()),
                userSignupRequestDto.getNickname(),
                profilePath
        );

        return userWriterRepository.save(user);
    }

    @Transactional
    @Override
    public User updateNickname(Long id, UserUpdateNicknameRequestDto userUpdateNicknameRequestDto) {

        // 1. 기존 사용자 조회

        User userExisting = userValidator.validateUserExists(id);

        userValidator.validateUserActive(userExisting);

        userValidator.validateUserNicknameDuplicate(userUpdateNicknameRequestDto.getNickname());

        // 2. 변경된 닉네임 반영
        User updatedUser = userExisting.withNickname(userUpdateNicknameRequestDto.getNickname());

        // 3. 게시글/댓글 작성자명 업데이트
        postWriteRepository.updateAuthorName(id, userUpdateNicknameRequestDto.getNickname());
        commentWriteRepository.updateAuthorName(id, userUpdateNicknameRequestDto.getNickname());

        // 4. 변경된 사용자 정보 저장
        return userWriterRepository.save(updatedUser);

    }

    @Transactional
    @Override
    public void updatePassword(Long id, UserUpdatePasswordRequestDto userUpdatePasswordRequestDto) {

        User userExisting = userValidator.validateUserExists(id);

        userValidator.validateUserActive(userExisting);

        // 현재 비밀번호 검증 (비밀번호 변경 시 추가 인증하도록 설정 - 나중에 고도화 때 진행)
//        if (!passwordEncoder.matches(userExisting.getPassword(), userUpdatePasswordRequestDto.getPassword())) {
//            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
//        }
        String updatedPassword = passwordEncoder.encode(userUpdatePasswordRequestDto.getPassword());

        User updated = userExisting.withPassword(updatedPassword);

        userWriterRepository.save(updated);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User user = userValidator.validateUserExists(id);

        userValidator.validateUserActive(user);

        User deletedUser = user.withIsActive(false);
        userWriterRepository.save(deletedUser);

        // 작성자명을 '알 수 없음'으로 일괄 업데이트 (JPQL)
        postWriteRepository.deletePostByUserId(id);
        commentWriteRepository.deleteCommentByUserId(id);

    }
}
