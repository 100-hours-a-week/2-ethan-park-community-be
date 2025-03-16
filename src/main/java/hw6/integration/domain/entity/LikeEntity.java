package hw6.integration.domain.entity;

import hw6.integration.domain.model.Like;
import hw6.integration.domain.model.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "likes")
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

    private boolean is_deleted;

    private Timestamp created_at;

    @Builder
    public LikeEntity(PostEntity postEntity, UserEntity userEntity, boolean is_deleted, Timestamp created_at) {
        this.postEntity = postEntity;
        this.userEntity = userEntity;
        this.is_deleted = is_deleted;
        this.created_at = created_at;
    }

    public Like toDomain() {
        return Like.builder()
                .id(this.id)
                .postId(this.postEntity.getId())
                .userId(this.userEntity.getId())
                .is_deleted(this.is_deleted)
                .created_at(this.created_at)
                .build();
    }

    public static LikeEntity fromDomain(Like like, PostEntity postEntity, UserEntity userEntity) {
        return LikeEntity.builder()
                .postEntity(postEntity)
                .userEntity(userEntity)
                .is_deleted(like.is_deleted())
                .created_at(like.getCreated_at())
                .build();

    }

}
