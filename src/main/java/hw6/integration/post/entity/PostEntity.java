package hw6.integration.post.entity;

import hw6.integration.image.entity.ImageEntity;
import hw6.integration.post.domain.Post;
import hw6.integration.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@EntityListeners(AuditingEntityListener.class) // ✅ 추가 필요
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private String authorName;

    @OneToMany(mappedBy = "postEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageEntity> images = new ArrayList<>();

    private Integer comment_count;
    private Integer like_count;
    private Integer view_count;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Builder(toBuilder = true)
    public PostEntity(Long id, UserEntity userEntity, String title, String content, String authorName, List<ImageEntity> images,
                      Integer comment_count, Integer like_count, Integer view_count,
                      LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.userEntity = userEntity;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.images = (images != null) ? images : new ArrayList<>(); // 직접 null 방어 처리
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
                .authorName(this.authorName)
                .comment_count(this.comment_count)
                .like_count(this.like_count)
                .view_count(this.view_count)
                .created_at(this.created_at)
                .updated_at(this.updated_at)
                .images(this.images != null
                        ? this.images.stream().map(ImageEntity::toDomain).toList()
                        : List.of())
                .build();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String title) {
        this.content = content;
    }

}
