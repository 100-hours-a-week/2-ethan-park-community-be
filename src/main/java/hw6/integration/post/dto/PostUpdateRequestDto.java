package hw6.integration.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostUpdateRequestDto {

    @NotBlank(message = "제목을 입력하세요.")
    private String title;

    @NotBlank(message = "이메일을 입력하세요.")
    private String content;

    private List<Long> imagesToDelete;

    private List<MultipartFile> newImages;
}
