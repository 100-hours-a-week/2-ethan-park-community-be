package hw6.integration.domain.model;

import hw6.integration.domain.entity.ImageEntity;
import hw6.integration.domain.entity.PostEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Image {

    private Long id;
    private Long postId;

    private String imagePath;

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
