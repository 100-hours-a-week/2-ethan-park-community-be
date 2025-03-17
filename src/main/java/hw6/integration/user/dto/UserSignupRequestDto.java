package hw6.integration.user.dto;

import hw6.integration.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserSignupRequestDto {

    private String email;
    private String password;
    private String nickname;

    private String profilePath;

    public User toDomain() {
        return User.builder()
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .profilePath(this.profilePath)
                .build();
    }
}
