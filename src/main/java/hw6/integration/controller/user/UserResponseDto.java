package hw6.integration.controller.user;

import hw6.integration.domain.model.User;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponseDto {

    private Long id;
    private String email;
    private String nickname;
    private String profilePath;
    private LocalDateTime createdAt;

    public static UserResponseDto toDomain(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profilePath(user.getProfilePath())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
