package hw6.integration.user.domain;

import hw6.integration.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;

@Getter
@Builder
@With
public class User {

    private Long id;

    private String email;

    private String password;

    private String nickname;

    private String profilePath;

    private Boolean isActive;

    private LocalDateTime createdAt;

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .profilePath(user.getProfilePath())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static User createUser(String email, String password, String nickname, String profilePath) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .profilePath(profilePath)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public User updateNickname(String nickname) {
        return this.withNickname(nickname);
    }

    public User updatePassword(String password) {
        return this.withPassword(password);
    }

}
