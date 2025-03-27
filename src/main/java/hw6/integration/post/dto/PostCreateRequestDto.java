package hw6.integration.post.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostCreateRequestDto {

    private String title;
    private String content;
    private List<MultipartFile> images;
}
