package hw6.integration.image.domain;

import hw6.integration.image.entity.ImageEntity;
import hw6.integration.post.entity.PostEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Image {

    private Long id;
    private Long postId;

    private String imagePath;


    // 나중에 유효성 검사 때 필요
    public Image(Long id, Long postId, String imagePath) {
        this.id = id;
        this.postId = postId;
        this.imagePath = imagePath;
    }

    public static ImageEntity toEntity(Image image, PostEntity postEntity) {
        return ImageEntity.builder()
                .postEntity(postEntity)
                .imagePath(image.getImagePath())
                .build();
    }
}
