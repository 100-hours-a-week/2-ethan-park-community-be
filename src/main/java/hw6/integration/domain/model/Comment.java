package hw6.integration.domain.model;

import hw6.integration.domain.entity.CommentEntity;
import hw6.integration.domain.entity.PostEntity;
import hw6.integration.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Builder
public class Comment {

    private Long id;
    private Long postId;
    private Long userId;

    private String content;

    private boolean is_deleted;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Comment(Long id, Long postId, Long userId, String content, boolean is_deleted,
                   LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.is_deleted = is_deleted;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public static CommentEntity toEntity(Comment comment, PostEntity postEntity, UserEntity userEntity) {
        return CommentEntity.builder()
                .postEntity(postEntity)
                .userEntity(userEntity)
                .content(comment.getContent())
                .is_deleted(comment.is_deleted())
                .created_at(comment.getCreated_at())
                .updated_at(comment.getUpdated_at())
                .build();
    }
}
