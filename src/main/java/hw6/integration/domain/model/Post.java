package hw6.integration.domain.model;

import hw6.integration.domain.entity.PostEntity;
import hw6.integration.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Post {

    private Long id;
    private Long userId;

    private String title;
    private String content;

    private Integer comment_count;
    private Integer like_count;
    private Integer view_count;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Post(Long id, Long userId, String title, String content, Integer comment_count, Integer like_count,
                Integer view_count, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.comment_count = comment_count;
        this.like_count = like_count;
        this.view_count = view_count;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public static PostEntity toEntity(Post post, UserEntity userEntity) {
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
