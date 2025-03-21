package hw6.integration.comment.domain;

import hw6.integration.comment.entity.CommentEntity;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;

@Getter
@Builder
@With
public class Comment {

    private Long id;
    private Long postId;
    private Long userId;

    private String authorName;
    private String content;

    private boolean isDeleted;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public static Comment createComment(Long postId, Long userId, String authorName, String content) {
        return Comment.builder()
                .postId(postId)
                .userId(userId)
                .authorName(authorName)
                .content(content)
                .isDeleted(false)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
    }

    public static CommentEntity toEntity(Comment comment, PostEntity postEntity, UserEntity userEntity) {
        return CommentEntity.builder()
                .postEntity(postEntity)
                .userEntity(userEntity)
                .authorName(comment.getAuthorName())
                .content(comment.getContent())
                .isDeleted(comment.isDeleted())
                .created_at(comment.getCreated_at())
                .updated_at(comment.getUpdated_at())
                .build();
    }

    private void maskAuthorName() {
        this.authorName = "알 수 없음";
    }
}
