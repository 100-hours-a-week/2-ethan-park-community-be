package hw6.integration.user.dto;

import hw6.integration.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponseDto {

    private Long id;
    private String email;
    private String nickname;
    private String profilePath;
    private LocalDateTime createdAt;

    public static UserResponseDto fromDomain(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profilePath(user.getProfilePath())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
