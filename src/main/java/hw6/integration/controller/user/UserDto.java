package hw6.integration.controller.user;

import hw6.integration.domain.model.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

    private Long id;
    private String email;
    private String nickname;
    private String profilePath;

    public static UserDto fromUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profilePath(user.getProfilePath())
                .build();
    }
}
