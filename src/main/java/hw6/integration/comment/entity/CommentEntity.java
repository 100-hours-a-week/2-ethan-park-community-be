package hw6.integration.comment.entity;

import hw6.integration.comment.domain.Comment;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
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

    private String authorName;
    private String content;

    private boolean isDeleted;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Builder
    public CommentEntity(PostEntity postEntity, UserEntity userEntity, String authorName, String content, boolean isDeleted,
                         LocalDateTime created_at, LocalDateTime updated_at) {
        this.postEntity = postEntity;
        this.userEntity = userEntity;
        this.authorName = authorName;
        this.content = content;
        this.isDeleted = isDeleted;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Comment toDomain() {
        return Comment.builder()
                .id(this.id)
                .postId(this.postEntity.getId())
                .userId(this.userEntity.getId())
                .authorName(this.authorName)
                .content(this.content)
                .isDeleted(this.isDeleted)
                .created_at(this.created_at)
                .updated_at(this.updated_at)
                .build();
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

}
