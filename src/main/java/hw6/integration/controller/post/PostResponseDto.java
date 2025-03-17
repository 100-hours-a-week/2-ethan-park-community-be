package hw6.integration.controller.post;

import hw6.integration.domain.model.Post;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Builder
public class PostResponseDto {

    private Long id;
    private Long userId;

    private String title;
    private String content;

    private Integer comment_count;
    private Integer like_count;
    private Integer view_count;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public static PostResponseDto fromPost(Post post){
        return PostResponseDto.builder()
                .id(post.getId())
                .userId(post.getUserId())
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
