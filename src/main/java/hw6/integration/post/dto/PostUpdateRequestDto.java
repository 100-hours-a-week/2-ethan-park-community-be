package hw6.integration.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostUpdateRequestDto {

    private String title;
    private String content;
    private List<Long> imagesToDelete;
    private List<MultipartFile> newImages;
}
