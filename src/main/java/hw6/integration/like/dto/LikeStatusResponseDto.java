package hw6.integration.like.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LikeStatusResponseDto {

    @JsonProperty("isLiked")
    private boolean isLiked;

    private int likeCount;

    public LikeStatusResponseDto (boolean isLiked, int likeCount) {

        this.isLiked = isLiked;
        this.likeCount = likeCount;
    }
}
