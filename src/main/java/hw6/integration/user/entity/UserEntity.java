package hw6.integration.user.entity;

import hw6.integration.post.entity.PostEntity;
import hw6.integration.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
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

    private Boolean isActive;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "userEntity")
    private List<PostEntity> posts = new ArrayList<>();

    @Builder
    public UserEntity(Long id, String email, String password, String nickname, String profilePath,
                      Boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profilePath = profilePath;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public User toDomain() {
        return User.builder()
                .id(this.id)
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .profilePath(this.profilePath)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .build();
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
