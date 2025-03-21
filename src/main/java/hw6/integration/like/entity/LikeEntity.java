package hw6.integration.like.entity;

import hw6.integration.like.domain.Like;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
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

    private boolean likeActive;

    private boolean isDeleted;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime created_at;

    @Builder
    public LikeEntity(PostEntity postEntity, UserEntity userEntity, boolean likeActive, boolean isDeleted, LocalDateTime created_at) {
        this.postEntity = postEntity;
        this.userEntity = userEntity;
        this.likeActive = likeActive;
        this.isDeleted = isDeleted;
        this.created_at = created_at;
    }

    public Like toDomain() {
        return Like.builder()
                .id(this.id)
                .postId(this.postEntity.getId())
                .userId(this.userEntity.getId())
                .likeActvie(this.likeActive)
                .isDeleted(this.isDeleted)
                .created_at(this.created_at)
                .build();
    }

}
