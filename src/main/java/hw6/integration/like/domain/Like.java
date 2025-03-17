package hw6.integration.like.domain;

import hw6.integration.like.entity.LikeEntity;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Like {

    private Long id;
    private Long postId;
    private Long userId;

    private boolean is_deleted;

    private LocalDateTime created_at;

    public Like(Long id, Long postId, Long userId, boolean is_deleted, LocalDateTime created_at) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.is_deleted = is_deleted;
        this.created_at = created_at;
    }

    public static LikeEntity toEntity(Like like, PostEntity postEntity, UserEntity userEntity) {
        return LikeEntity.builder()
                .postEntity(postEntity)
                .userEntity(userEntity)
                .is_deleted(like.is_deleted())
                .created_at(like.getCreated_at())
                .build();

    }
}
