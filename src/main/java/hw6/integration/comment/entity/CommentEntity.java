package hw6.integration.comment.entity;

import hw6.integration.AuditEntity;
import hw6.integration.comment.domain.Comment;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor
public class CommentEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id") // FK 컬럼명
    private PostEntity postEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // FK 컬럼명
    private UserEntity userEntity;

    @Column(nullable = false)
    private String authorName;
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isDeleted;


    @Builder
    public CommentEntity(PostEntity postEntity, UserEntity userEntity, String authorName, String content, boolean isDeleted) {
        this.postEntity = postEntity;
        this.userEntity = userEntity;
        this.authorName = authorName;
        this.content = content;
        this.isDeleted = isDeleted;
    }

    public Comment toDomain() {
        return Comment.builder()
                .id(this.id)
                .postId(this.postEntity.getId())
                .userId(this.userEntity.getId())
                .authorName(this.authorName)
                .content(this.content)
                .isDeleted(this.isDeleted)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
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
