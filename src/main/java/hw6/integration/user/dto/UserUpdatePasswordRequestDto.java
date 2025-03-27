package hw6.integration.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdatePasswordRequestDto {

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

}
