package hw6.integration.domain.entity;

import hw6.integration.domain.model.Image;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images")
@NoArgsConstructor
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity postEntity;

    private String imagePath;

    @Builder
    public ImageEntity(PostEntity postEntity, String imagePath) {
        this.postEntity = postEntity;
        this.imagePath = imagePath;
    }

    public Image toDomain() {
        return Image.builder()
                .id(this.id)
                .postId(this.postEntity.getId())
                .imagePath(this.imagePath)
                .build();
    }

    public static ImageEntity fromDomain(Image image, PostEntity postEntity) {
        return ImageEntity.builder()
                .postEntity(postEntity)
                .imagePath(image.getImagePath())
                .build();
    }

}
