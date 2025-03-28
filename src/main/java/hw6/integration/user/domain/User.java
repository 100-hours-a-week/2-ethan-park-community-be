package hw6.integration.user.domain;

import hw6.integration.post.domain.Post;
import hw6.integration.user.entity.UserEntity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@With
@EqualsAndHashCode
public class User {

    private Long id;

    private String email;

    private String password;

    private String nickname;

    private String profilePath;

    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    public static User createUser(String email, String password, String nickname, String profilePath) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .profilePath(profilePath)
                .isActive(true)
                .build();
    }

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .profilePath(user.getProfilePath())
                .isActive(user.getIsActive())
                .build();
    }

    public boolean isActiveUser() {
        return Boolean.TRUE.equals(this.isActive);
    }

}
