package hw6.integration.user.service;

import hw6.integration.comment.domain.Comment;
import hw6.integration.comment.repository.CommentRepository;
import hw6.integration.config.JwtProvider;
import hw6.integration.image.component.ImageComponent;
import hw6.integration.post.domain.Post;
import hw6.integration.post.repository.PostRepository;
import hw6.integration.user.dto.UserLoginRequestDto;
import hw6.integration.user.dto.UserSignupRequestDto;
import hw6.integration.user.dto.UserUpdateNicknameRequestDto;
import hw6.integration.user.dto.UserUpdatePasswordRequestDto;
import hw6.integration.user.domain.User;
import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ImageComponent imageComponent;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public List<User> getUserByAll() {

        return userRepository.findByAll();
    }

    @Override
    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    }

    @Transactional
    @Override
    public User registerUser(UserSignupRequestDto userSignupRequestDto) {

        if(userRepository.findByEmail(userSignupRequestDto.getEmail()).isPresent()){
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATE);
        }

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

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public String login(UserLoginRequestDto userLoginRequestDto) {
        User user = userRepository.findByEmail(userLoginRequestDto.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        return jwtProvider.generateToken(user);
    }

    @Transactional
    @Override
    public User updateNickname(Long id, UserUpdateNicknameRequestDto userUpdateNicknameRequestDto) {

        // 1. 기존 사용자 조회
        User userExisting = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. 변경된 닉네임 반영
        User updatedUser = userExisting.withNickname(userUpdateNicknameRequestDto.getNickname());

        // 3. 게시글/댓글 작성자명 업데이트
        postRepository.updateAuthorName(id, userUpdateNicknameRequestDto.getNickname());
        commentRepository.updateAuthorName(id, userUpdateNicknameRequestDto.getNickname());

        // 4. 변경된 사용자 정보 저장
        return userRepository.save(updatedUser);

    }

    @Transactional
    @Override
    public void updatePassword(Long id, UserUpdatePasswordRequestDto userUpdatePasswordRequestDto) {

        User userExisting = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 현재 비밀번호 검증 (비밀번호 변경 시 추가 인증하도록 설정 - 나중에 고도화 때 진행)
//        if (!passwordEncoder.matches(userExisting.getPassword(), userUpdatePasswordRequestDto.getPassword())) {
//            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
//        }

        User updated = userExisting.withPassword(userUpdatePasswordRequestDto.getPassword());

        userRepository.save(updated);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        User deletedUser = user.withIsActive(false);
        userRepository.save(deletedUser);

        // 작성자명을 '알 수 없음'으로 일괄 업데이트 (JPQL)
        postRepository.deletePostByUserId(id, true, "알 수 없음");
        commentRepository.deleteCommentByUserId(id, true, "알 수 없음");

    }
}
