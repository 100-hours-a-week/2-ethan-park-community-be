package hw6.integration.like.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LikeStatusResponseDto {

    @JsonProperty("isLiked")
    private boolean isLiked;

    private int likeCount;

}
