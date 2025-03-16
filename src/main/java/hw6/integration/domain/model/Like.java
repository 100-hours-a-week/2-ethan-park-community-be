package hw6.integration.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class Like {

    private Long id;
    private Long postId;
    private Long userId;

    private boolean is_deleted;

    private Timestamp created_at;

    public Like(Long id, Long postId, Long userId, boolean is_deleted, Timestamp created_at) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.is_deleted = is_deleted;
        this.created_at = created_at;
    }
}
