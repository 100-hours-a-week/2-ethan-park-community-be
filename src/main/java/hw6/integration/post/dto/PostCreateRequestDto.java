package hw6.integration.post.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PostCreateRequestDto {

    private String title;
    private String content;
    private List<MultipartFile> images;
}
