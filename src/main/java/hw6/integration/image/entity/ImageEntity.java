package hw6.integration.image.entity;

import hw6.integration.image.domain.Image;
import hw6.integration.post.entity.PostEntity;
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



}
