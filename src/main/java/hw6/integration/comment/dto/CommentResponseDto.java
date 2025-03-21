package hw6.integration.comment.dto;

import hw6.integration.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {

    private Long id;
    private Long postId;
    private Long userId;

    private String authorName;
    private String content;

    private boolean isDeleted;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public static CommentResponseDto fromComment(Comment comment) {

        return CommentResponseDto.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .userId(comment.getUserId())
                .authorName(comment.getAuthorName())
                .content(comment.getContent())
                .isDeleted(comment.isDeleted())
                .created_at(comment.getCreated_at())
                .updated_at(comment.getUpdated_at())
                .build();

    }
}
