package hw6.integration.user.controller;

import hw6.integration.user.dto.UserSignupRequestDto;
import hw6.integration.user.service.UserAuthService;
import hw6.integration.user.service.UserReadService;
import hw6.integration.user.service.UserWriterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserWriterService userWriterService;

    @Mock
    private UserAuthService userAuthService;

    @Mock
    private UserReadService userReadService;

    @InjectMocks
    private UserController userController;

    @Test
    void should_success_signup() {
        // given
        MockMultipartFile image = new MockMultipartFile(
                "profileImage",                     // 필드명 (dto의 필드명과 같아야 함)
                "profile.png",                      // 파일명
                "image/png",                        // MIME 타입
                "이미지바이트".getBytes());
        UserSignupRequestDto userSignupRequestDto = new UserSignupRequestDto();
        userSignupRequestDto.setEmail("test@naver.com");
        userSignupRequestDto.setPassword("password");
        userSignupRequestDto.setNickname("test");
        userSignupRequestDto.setProfileImage(image);

        // when
        ResponseEntity<Void> response = userController.registerUser(userSignupRequestDto);

        // then
        assertThat(HttpStatus.CREATED).isEqualTo(response.getStatusCode());
        verify(userWriterService, times(1)).registerUser(userSignupRequestDto);
    }

}