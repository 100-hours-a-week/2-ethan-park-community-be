package hw6.integration.controller.user;

import hw6.integration.domain.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class UserCreateRequestDto {

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
