package hw6.integration.user.service;

import hw6.integration.comment.repository.CommentWriteRepository;
import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.image.component.ImageComponent;
import hw6.integration.post.repository.PostWriteRepository;
import hw6.integration.user.domain.User;
import hw6.integration.user.dto.UserSignupRequestDto;
import hw6.integration.user.dto.UserUpdateNicknameRequestDto;
import hw6.integration.user.dto.UserUpdatePasswordRequestDto;
import hw6.integration.user.repository.UserReadRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;


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
    @Mock
    private UserReadRepository userReadRepository;

    @InjectMocks
    private UserWriterServiceImpl userWriterService;

    @Test
    @DisplayName("회원가입 테스트 - 모든 데이터가 정상적으로 입력됐을 경우")
    void should_register_user_successfully_with_user() {

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
        given(userWriterRepository.save(any(User.class)))
                .willAnswer(invocation -> invocation.getArgument(0));


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
    @DisplayName("회원가입 테스트 - 이메일이 중복인 경우")
    void should_throw_exception_when_email_is_duplicate() {

        //given
        String duplicateEmail = "test@naver.com";

        UserSignupRequestDto userSignupRequestDto = new UserSignupRequestDto();
        userSignupRequestDto.setEmail(duplicateEmail);
        userSignupRequestDto.setNickname("test");
        userSignupRequestDto.setPassword("password");


        // 💡 이메일 중복 검사 시 예외 발생하도록 설정
        doThrow(new BusinessException(ErrorCode.EMAIL_DUPLICATE))
                .when(userValidator).validateUserEmailDuplicate(duplicateEmail);

        //when & then
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userWriterService.registerUser(userSignupRequestDto)
        );

        assertEquals(ErrorCode.EMAIL_DUPLICATE, exception.getErrorCode());

        verify(userValidator).validateUserEmailDuplicate(duplicateEmail);
        verify(userValidator, never()).validateUserNicknameDuplicate(any());
        verify(userWriterRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
        verify(imageComponent, never()).uploadProfileImage(any());
    }

    @Test
    @DisplayName("회원가입 테스트 - 닉네임이 중복인 경우")
    void should_throw_exception_when_nickname_is_duplicate() {

        //given
        String duplicateNickname = "test";

        UserSignupRequestDto userSignupRequestDto = new UserSignupRequestDto();
        userSignupRequestDto.setNickname(duplicateNickname);
        userSignupRequestDto.setEmail("test@naver.com");
        userSignupRequestDto.setPassword("password");

        doThrow(new BusinessException(ErrorCode.NICKNAME_DUPLICATE))
                .when(userValidator).validateUserNicknameDuplicate(duplicateNickname);

        //when & then
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userWriterService.registerUser(userSignupRequestDto)
        );

        assertEquals(ErrorCode.NICKNAME_DUPLICATE, exception.getErrorCode());

        verify(userValidator).validateUserNicknameDuplicate(duplicateNickname);
        verify(userWriterRepository, never()).save(any());
        verify(imageComponent, never()).uploadProfileImage(any());
        verify(passwordEncoder, never()).encode(any());

    }

    @Test
    @DisplayName("회원가입 테스트 - 프로필 사진이 없을 경우")
    void should_throw_exception_when_profile_image_is_empty() {


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
    @DisplayName("비밀번호 변경 테스트")
    void updatePassword_success() {

        //given
        Long userId = 1L;
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        UserUpdatePasswordRequestDto userUpdatePasswordRequestDto = new UserUpdatePasswordRequestDto();
        userUpdatePasswordRequestDto.setPassword(newPassword);

        User user = User.builder()
                .id(userId)
                .email("user@naver.com")
                .password(oldPassword)
                .nickname("user")
                .profilePath("/image_storage/photo.jpg")
                .isActive(true)
                .build();


        given(userValidator.validateUserExists(userId)).willReturn(user);
        given(passwordEncoder.encode(newPassword)).willReturn("encryptedPassword");
        given(userWriterRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0)); // 저장값 그대로 반환

        //when
        userWriterService.updatePassword(userId, userUpdatePasswordRequestDto);


        //then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userWriterRepository).save(captor.capture());
        User captureUser = captor.getValue();

        assertEquals("encryptedPassword", captureUser.getPassword());

        verify(userValidator).validateUserExists(userId);
        verify(userValidator).validateUserActive(user);
        verify(passwordEncoder).encode(newPassword);

    }

    @Test
    @DisplayName("회원 탈퇴 시 사용자 비활성화되고 관련 글/댓글 처리")
    void deleteUser_success() {

        //given
        Long userId = 1L;

        User user = User.builder()
                .id(userId)
                .email("user@naver.com")
                .password("password")
                .nickname("user")
                .profilePath("/image_storage/photo.jpg")
                .isActive(true)
                .build();

        User deletedUser = user.withIsActive(false);

        willDoNothing().given(userValidator).validateUserActive(user);
        given(userValidator.validateUserExists(userId)).willReturn(user);
        given(userWriterRepository.save(any(User.class))).willReturn(deletedUser);

        //when
        userWriterService.deleteUser(userId);

        //then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userWriterRepository).save(captor.capture());
        User captureUser = captor.getValue();

        assertFalse(captureUser.getIsActive());

        verify(userValidator).validateUserExists(userId);
        verify(userValidator).validateUserActive(user);
        verify(userWriterRepository).save(any(User.class));
        verify(postWriteRepository).deletePostByUserId(userId);
        verify(commentWriteRepository).deleteCommentByUserId(userId);
    }
}