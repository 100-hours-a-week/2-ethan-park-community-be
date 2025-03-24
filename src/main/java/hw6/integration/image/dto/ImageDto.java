package hw6.integration.image.dto;

import hw6.integration.image.domain.Image;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageDto {
    private Long id;
    private String imagePath;

    public static ImageDto from(Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .imagePath(image.getImagePath())
                .build();
    }
}
