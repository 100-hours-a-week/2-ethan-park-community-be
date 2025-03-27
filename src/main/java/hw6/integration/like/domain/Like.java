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

    // 변수 통합?
    private boolean likeActvie;

    private boolean isDeleted;

    private LocalDateTime createdAt;

    public static Like createLike(Long postId, Long userId) {
        return Like.builder()
                .postId(postId)
                .userId(userId)
                .likeActvie(true)
                .isDeleted(false)
                .build();
    }

    public static LikeEntity toEntity(Like like, PostEntity postEntity, UserEntity userEntity) {
        return LikeEntity.builder()
                .postEntity(postEntity)
                .userEntity(userEntity)
                .likeActive(like.isLikeActvie())
                .isDeleted(like.isDeleted())
                .build();

    }
}
