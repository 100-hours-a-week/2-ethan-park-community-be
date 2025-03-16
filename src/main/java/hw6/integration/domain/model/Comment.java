package hw6.integration.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class Comment {

    private Long id;
    private Long postId;
    private Long userId;

    private String content;

    private boolean is_deleted;

    private Timestamp created_at;
    private Timestamp updated_at;

    public Comment(Long id, Long postId, Long userId, String content, boolean is_deleted,
                   Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.is_deleted = is_deleted;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
