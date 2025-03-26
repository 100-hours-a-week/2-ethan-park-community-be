package hw6.integration.user.dto;

import hw6.integration.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserNicknameUpdateResponseDto {

    private String nickname;

    public static UserNicknameUpdateResponseDto fromDomain(User user) {
        return UserNicknameUpdateResponseDto.builder()
                .nickname(user.getNickname())
                .build();
    }
}
