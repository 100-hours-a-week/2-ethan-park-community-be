package hw6.integration.like.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeStatusResponseDto {

    @JsonProperty("isLiked")
    private boolean isLiked;

    private int likeCount;

    public static LikeStatusResponseDto from(boolean isLiked, int likeCount) {
        return LikeStatusResponseDto.builder()
                .isLiked(isLiked)
                .likeCount(likeCount)
                .build();
    }

}
