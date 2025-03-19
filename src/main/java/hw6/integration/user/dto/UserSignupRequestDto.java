package hw6.integration.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@RequiredArgsConstructor
public class UserSignupRequestDto {

    private String email;
    private String password;
    private String nickname;
    private MultipartFile profileImage;
}
