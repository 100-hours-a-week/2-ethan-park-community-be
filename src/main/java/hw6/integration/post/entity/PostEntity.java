package hw6.integration.post.entity;

import hw6.integration.AuditEntity;
import hw6.integration.image.entity.ImageEntity;
import hw6.integration.post.domain.Post;
import hw6.integration.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@EntityListeners(AuditingEntityListener.class) // ✅ 추가 필요
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK 설정 (Post.user → User.id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // FK 컬럼명
    private UserEntity userEntity;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String authorName;

    @OneToMany(mappedBy = "postEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageEntity> images = new ArrayList<>();

    @Column(nullable = false)
    private int comment_count;
    @Column(nullable = false)
    private int like_count;
    @Column(nullable = false)
    private int view_count;

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder(toBuilder = true)
    public PostEntity(Long id, UserEntity userEntity, String title, String content, String authorName, List<ImageEntity> images,
                      Integer comment_count, Integer like_count, Integer view_count, boolean isDeleted) {
        this.id = id;
        this.userEntity = userEntity;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.images = (images != null) ? images : new ArrayList<>(); // 직접 null 방어 처리
        this.comment_count = comment_count;
        this.like_count = like_count;
        this.view_count = view_count;
        this.isDeleted = isDeleted;
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
                .isDeleted(this.isDeleted)
                .images(this.images != null
                        ? this.images.stream().map(ImageEntity::toDomain).toList()
                        : List.of())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }


    // JPA 영속성 컨텍스트를 이용한 상태 변경이라 entity에서 처리하는 게 맞다
    public void update(String title, String content) {
        if (title != null) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }
    }

    public void incrementCommentCount() {
        this.comment_count++;
    }

    public void decrementCommentCount() {
        this.comment_count--;
    }

    public void incrementViewCount() {
        this.view_count++;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


}
