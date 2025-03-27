package hw6.integration.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateNicknameRequestDto {

    @NotBlank(message = "닉네임을 입력하세요.")
    private String nickname;

}
