package hw6.integration.domain.entity;

import hw6.integration.domain.model.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@Getter
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // FK 설정 (Post.user → User.id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // FK 컬럼명
    private UserEntity userEntity;

    private String title;

    private String content;

    private Integer comment_count;

    private Integer like_count;

    private Integer view_count;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Builder
    public PostEntity(UserEntity userEntity, String title, String content, Integer comment_count, Integer like_count,
                      Integer view_count, LocalDateTime created_at, LocalDateTime updated_at) {
        this.userEntity = userEntity;
        this.title = title;
        this.content = content;
        this.comment_count = comment_count;
        this.like_count = like_count;
        this.view_count = view_count;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Post toDomain() {
        return Post.builder()
                .id(this.id)
                .userId(this.userEntity.getId())
                .title(this.title)
                .content(this.content)
                .comment_count(this.comment_count)
                .like_count(this.like_count)
                .view_count(this.view_count)
                .created_at(this.created_at)
                .updated_at(this.updated_at)
                .build();
    }
}
