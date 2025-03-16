package hw6.integration.domain.entity;

import hw6.integration.domain.model.Comment;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "comments")
@NoArgsConstructor
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id") // FK 컬럼명
    private PostEntity postEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // FK 컬럼명
    private UserEntity userEntity;

    private String content;

    private boolean is_deleted;

    private Timestamp created_at;
    private Timestamp updated_at;

    @Builder
    public CommentEntity(PostEntity postEntity, UserEntity userEntity, String content, boolean is_deleted, Timestamp created_at, Timestamp updated_at) {
        this.postEntity = postEntity;
        this.userEntity = userEntity;
        this.content = content;
        this.is_deleted = is_deleted;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Comment toDomain() {
        return Comment.builder()
                .id(this.id)
                .postId(this.postEntity.getId())
                .userId(this.userEntity.getId())
                .content(this.content)
                .is_deleted(this.is_deleted)
                .created_at(this.created_at)
                .updated_at(this.updated_at)
                .build();
    }

    public static CommentEntity fromDomain(Comment comment, PostEntity postEntity, UserEntity userEntity) {
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
