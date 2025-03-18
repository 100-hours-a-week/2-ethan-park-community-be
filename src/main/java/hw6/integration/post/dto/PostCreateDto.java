package hw6.integration.post.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostCreateDto {

    private String title;
    private String content;

}
