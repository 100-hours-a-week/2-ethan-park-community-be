package hw6.integration.domain.entity;

import hw6.integration.domain.model.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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

    private Timestamp created_at;

    private Timestamp updated_at;

    @Builder
    public PostEntity(UserEntity userEntity, String title, String content, Integer comment_count, Integer like_count,
                      Integer view_count, Timestamp created_at, Timestamp updated_at) {
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

    public static PostEntity fromDomain(Post post, UserEntity userEntity) {
        return PostEntity.builder()
                .userEntity(userEntity)
                .title(post.getTitle())
                .content(post.getContent())
                .comment_count(post.getComment_count())
                .like_count(post.getLike_count())
                .view_count(post.getView_count())
                .created_at(post.getCreated_at())
                .updated_at(post.getUpdated_at())
                .build();
    }
}
