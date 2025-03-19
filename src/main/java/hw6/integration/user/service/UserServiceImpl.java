package hw6.integration.user.service;

import hw6.integration.config.JwtProvider;
import hw6.integration.post.domain.Post;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.post.repository.PostRepository;
import hw6.integration.user.dto.UserLoginRequestDto;
import hw6.integration.user.dto.UserSignupRequestDto;
import hw6.integration.user.dto.UserUpdateNicknameRequestDto;
import hw6.integration.user.dto.UserUpdatePasswordRequestDto;
import hw6.integration.user.domain.User;
import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.user.entity.UserEntity;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public List<User> getUserByAll() {

        return userRepository.findByAll()
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    }

    @Override
    public User registerUser(UserSignupRequestDto userSignupRequestDto) {

        if(userRepository.findByEmail(userSignupRequestDto.getEmail()).isPresent()){
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATE);
        }

        User user = User.createUser(
                userSignupRequestDto.getEmail(),
                passwordEncoder.encode(userSignupRequestDto.getPassword()),
                userSignupRequestDto.getNickname(),
                userSignupRequestDto.getProfilePath()
        );

        return userRepository.save(user);
    }

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
    public User updateNickname(Long id,  UserUpdateNicknameRequestDto userUpdateNicknameRequestDto) {

        User userExisting = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (userExisting.isActiveUser()){
            User updatedUser = userExisting.withNickname(userUpdateNicknameRequestDto.getNickname());

            List<Post> posts = postRepository.findByUserId(id);
            List<Post> updatedPosts = posts.stream()
                    .map(post -> post.withAuthorName(userUpdateNicknameRequestDto.getNickname()))
                    .toList();
            postRepository.saveAll(updatedPosts, User.toEntity(updatedUser));

            return userRepository.save(updatedUser);
        }

        throw new BusinessException(ErrorCode.USER_NOT_FOUND);
    }

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

        List<Post> posts = postRepository.findByUserId(id);
        posts.forEach(Post::maskAuthorName);
        postRepository.saveAll(posts, User.toEntity(deletedUser));

        userRepository.save(deletedUser);
    }
}
