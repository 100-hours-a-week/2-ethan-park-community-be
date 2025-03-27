package hw6.integration.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequestDto {

    @NotBlank(message = "내용을 입력하세요.")
    private String content;
}
