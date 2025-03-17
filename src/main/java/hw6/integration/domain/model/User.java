package hw6.integration.domain.model;

import hw6.integration.domain.entity.UserEntity;
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

    private Boolean is_active;

    private LocalDateTime createdAt;

    public User(Long id, String email, String password, String nickname, String profilePath,
                Boolean is_active, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profilePath = profilePath;
        this.createdAt = createdAt;
        this.is_active = is_active;
    }

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .profilePath(user.getProfilePath())
                .is_active(user.getIs_active())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public User updateNickname(String nickname) {
        return this.withNickname(nickname);
    }

    public User updatePassword(String password) {
        return this.withPassword(password);
    }

}
