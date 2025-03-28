package hw6.integration.like.entity;

import hw6.integration.like.domain.Like;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"})
)
@NoArgsConstructor
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK 설정 (Post.user → User.id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id") // FK 컬럼명
    private PostEntity postEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // FK 컬럼명
    private UserEntity userEntity;

    @Column(name = "like_active", nullable = false)
    private boolean likeActive;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public LikeEntity(PostEntity postEntity, UserEntity userEntity, boolean likeActive, boolean isDeleted, LocalDateTime createdAt) {
        this.postEntity = postEntity;
        this.userEntity = userEntity;
        this.likeActive = likeActive;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
    }

    public Like toDomain() {
        return Like.builder()
                .id(this.id)
                .postId(this.postEntity.getId())
                .userId(this.userEntity.getId())
                .likeActvie(this.likeActive)
                .isDeleted(this.isDeleted)
                .createdAt(this.createdAt)
                .build();
    }

}
