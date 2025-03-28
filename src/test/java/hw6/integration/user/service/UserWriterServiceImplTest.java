package hw6.integration.user.service;

import hw6.integration.comment.repository.CommentWriteRepository;
import hw6.integration.image.component.ImageComponent;
import hw6.integration.post.repository.PostWriteRepository;
import hw6.integration.user.domain.User;
import hw6.integration.user.dto.UserSignupRequestDto;
import hw6.integration.user.dto.UserUpdateNicknameRequestDto;
import hw6.integration.user.repository.UserWriterRepository;
import hw6.integration.user.util.UserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class UserWriterServiceImplTest {

    @Mock
    private UserWriterRepository userWriterRepository;
    @Mock
    private ImageComponent imageComponent;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserValidator userValidator;
    @Mock
    private PostWriteRepository postWriteRepository;
    @Mock
    private CommentWriteRepository commentWriteRepository;

    @InjectMocks
    private UserWriterServiceImpl userWriterService;

    @Test
    @DisplayName("회원가입 테스트")
    void registerUser_success() {

        MockMultipartFile profileImage = new MockMultipartFile(
                "profileImage",
                "image.png",
                "image/png",
                "image data".getBytes()
        );
        // 테스트를 시작할 때마다 User를 새로 생성
        UserSignupRequestDto userSignupRequestDto = new UserSignupRequestDto();
        userSignupRequestDto.setEmail("user@naver.com");
        userSignupRequestDto.setPassword("User1234@");
        userSignupRequestDto.setNickname("user");
        userSignupRequestDto.setProfileImage(profileImage);

        //given
        given(imageComponent.uploadProfileImage(profileImage)).willReturn("/image_storage/profile.png");
        given(passwordEncoder.encode(userSignupRequestDto.getPassword())).willReturn("EncodePassword");

        User user = User.builder()
                .id(1L)
                .email(userSignupRequestDto.getEmail())
                .password("EncodePassword")
                .nickname(userSignupRequestDto.getNickname())
                .profilePath("/image_storage/profile.png")
                .isActive(true)
                .build();

        given(userWriterRepository.save(any(User.class))).willReturn(user);

        //when
        User registerdUser = userWriterService.registerUser(userSignupRequestDto);

        //then
        assertNotNull(registerdUser);
        assertEquals("user@naver.com", registerdUser.getEmail());
        assertEquals("EncodePassword", registerdUser.getPassword());
        assertEquals("user", registerdUser.getNickname());
        assertEquals("/image_storage/profile.png", registerdUser.getProfilePath());

        // ArgumentCaptor 사용
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userWriterRepository).save(captor.capture());
        User savedUser = captor.getValue();

        assertEquals("user@naver.com", savedUser.getEmail());
        assertEquals("EncodePassword", savedUser.getPassword());
        assertEquals("user", savedUser.getNickname());
        assertEquals("/image_storage/profile.png", savedUser.getProfilePath());

        verify(userValidator).validateUserEmailDuplicate("user@naver.com");
        verify(userValidator).validateUserNicknameDuplicate("user");
        verify(imageComponent).uploadProfileImage(profileImage);
        verify(passwordEncoder).encode("User1234@");

    }

    @Test
    @DisplayName("사용자 닉네임 변경 테스트")
    void updateNickname_success() {

        //given
        Long userId = 1L;
        String oldNickname = "oldNickname";
        String newNickname = "newNickname";
        UserUpdateNicknameRequestDto userNicknameUpdateResponseDto = new UserUpdateNicknameRequestDto();
        userNicknameUpdateResponseDto.setNickname(newNickname);

        User user = User.builder()
                .id(userId)
                .email("user@naver.com")
                .password("User1234@")
                .nickname(oldNickname)
                .profilePath("/image_storage/profile.png")
                .isActive(true)
                .build();

        User updatedUser = user.withNickname(newNickname);

        given(userValidator.validateUserExists(userId)).willReturn(user);
        given(userWriterRepository.save(any(User.class))).willReturn(updatedUser);

        //when
        User result = userWriterService.updateNickname(userId, userNicknameUpdateResponseDto);

        //then
        assertNotNull(result);
        assertEquals(newNickname, result.getNickname());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userWriterRepository).save(captor.capture());
        User captureUser = captor.getValue();

        assertEquals(newNickname, captureUser.getNickname());

        verify(userValidator).validateUserExists(userId);
        verify(userValidator).validateUserActive(user);
        verify(userValidator).validateUserNicknameDuplicate(newNickname);
        verify(postWriteRepository).updateAuthorName(userId, newNickname);
        verify(commentWriteRepository).updateAuthorName(userId, newNickname);

    }

    @Test
    void updatePassword() {
    }

    @Test
    void deleteUser() {
    }
}