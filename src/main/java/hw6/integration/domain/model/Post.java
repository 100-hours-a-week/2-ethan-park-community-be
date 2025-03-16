package hw6.integration.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

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

    private Timestamp created_at;
    private Timestamp updated_at;

    public Post(Long id, Long userId, String title, String content, Integer comment_count, Integer like_count,
                Integer view_count, Timestamp created_at, Timestamp updated_at) {
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
}
