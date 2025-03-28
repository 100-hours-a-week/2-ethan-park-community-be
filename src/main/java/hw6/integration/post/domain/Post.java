package hw6.integration.post.domain;

import hw6.integration.image.domain.Image;
import hw6.integration.image.entity.ImageEntity;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@With
public class Post {

    private Long id;
    private Long userId;

    private String title;
    private String content;
    private String authorName;

    @Builder.Default
    private List<Image> images = new ArrayList<>();

    private Integer commentCount;
    private Integer likeCount;
    private Integer viewCount;

    private boolean isDeleted;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Post createPost(Long userId, String title, String content, String authorName) {
        return Post.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .authorName(authorName)
                .commentCount(0)
                .likeCount(0)
                .viewCount(0)
                .isDeleted(false)
                .build();

    }

    public static PostEntity toEntity(Post post, UserEntity userEntity) {
        PostEntity postEntity = PostEntity.builder()
                .id(post.getId())
                .userEntity(userEntity)
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.getAuthorName())
                .commentCount(post.getCommentCount())
                .likeCount(post.getLikeCount())
                .viewCount(post.getViewCount())
                .isDeleted(post.isDeleted())
                .build();

        // 이미지 리스트가 존재하면 PostEntity에 추가
        if (post.getImages() != null && !post.getImages().isEmpty()) {
            List<ImageEntity> imageEntities = post.getImages().stream()
                    .map(image -> Image.toEntity(image, postEntity)) // postEntity로 연관관계 맺기
                    .toList();
            postEntity.getImages().addAll(imageEntities);
        }

        return postEntity;
    }

    public void addImages(List<Image> images) {
        if (images.size() > 10) {
            throw new IllegalArgumentException("이미지는 최대 10장까지 등록 가능합니다.");
        }
        this.images = images;
    }


}
