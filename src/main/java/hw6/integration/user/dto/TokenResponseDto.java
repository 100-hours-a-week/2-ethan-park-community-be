package hw6.integration.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponseDto {

    private final String token;

    public static TokenResponseDto from(String token) {
        return TokenResponseDto.builder()
                .token(token)
                .build();
    }
}
