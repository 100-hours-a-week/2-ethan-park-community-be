package hw6.integration.domain.entity;

import hw6.integration.domain.model.Comment;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Builder
    public CommentEntity(PostEntity postEntity, UserEntity userEntity, String content, boolean is_deleted,
                         LocalDateTime created_at, LocalDateTime updated_at) {
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


}
