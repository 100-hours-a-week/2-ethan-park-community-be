package hw6.integration.domain.entity;

import hw6.integration.domain.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String nickname;

    private String profilePath;

    private Boolean is_active;

    private Timestamp createdAt;

    @Builder
    public UserEntity(String email, String password, String nickname, String profilePath,
                      Boolean is_active, Timestamp createdAt) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profilePath = profilePath;
        this.createdAt = createdAt;
        this.is_active = is_active;
    }

    public User toDomain() {
        return User.builder()
                .id(this.id)
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .profilePath(this.profilePath)
                .is_active(this.is_active)
                .createdAt(this.createdAt)
                .build();
    }

    public static UserEntity fromDomain(User user) {
        return UserEntity.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .profilePath(user.getProfilePath())
                .is_active(user.getIs_active())
                .createdAt(user.getCreatedAt())
                .build();
    }

}
