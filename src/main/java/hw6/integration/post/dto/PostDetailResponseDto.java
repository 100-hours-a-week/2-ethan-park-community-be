package hw6.integration.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

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
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .images(post.getImages().stream().map(ImageDto::from).toList()) // ✅ 이 라인 확인
                .build();
    }
}
