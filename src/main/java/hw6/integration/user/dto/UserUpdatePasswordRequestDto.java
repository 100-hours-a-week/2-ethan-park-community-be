package hw6.integration.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserUpdatePasswordRequestDto {

    private String password;

}
