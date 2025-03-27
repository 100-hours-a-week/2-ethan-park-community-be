package hw6.integration.post.dto;

import hw6.integration.image.dto.ImageDto;
import hw6.integration.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostDetailResponseDto {

    private Long id;
    private Long userId;

    private String title;
    private String content;
    private String authorName;

    private List<ImageDto> images; // ✅ 있어야 함

    private Integer comment_count;
    private Integer like_count;
    private Integer view_count;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public static PostDetailResponseDto fromPost(Post post) {
        return PostDetailResponseDto.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.getAuthorName())
                .comment_count(post.getComment_count())
                .like_count(post.getLike_count())
                .view_count(post.getView_count())
                .created_at(post.getCreated_at())
                .updated_at(post.getUpdated_at())
                .images(post.getImages().stream().map(ImageDto::from).toList()) // ✅ 이 라인 확인
                .build();
    }
}
