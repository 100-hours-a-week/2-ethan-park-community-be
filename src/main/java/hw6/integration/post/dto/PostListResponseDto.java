package hw6.integration.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hw6.integration.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostListResponseDto {

    private Long id;
    private Long userId;

    private String title;
    private String content;
    private String authorName;

    private Integer comment_count;
    private Integer like_count;
    private Integer view_count;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public static PostListResponseDto fromPost(Post post) {
        return PostListResponseDto.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.getAuthorName())
                .comment_count(post.getCommentCount())
                .like_count(post.getLikeCount())
                .view_count(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

}
