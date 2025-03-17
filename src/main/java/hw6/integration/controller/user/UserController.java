package hw6.integration.controller.user;

import hw6.integration.domain.model.User;
import hw6.integration.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = userService.getUserByAll();

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // 상태 코드 204
        }

        List<UserResponseDto> userResponseDtos = users.stream()
                .map(UserResponseDto::toDomain)
                .toList();

        return ResponseEntity.ok(userResponseDtos); // 상태 코드 200
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {

        User user = userService.getUserById(id);

        UserResponseDto userResponseDto = UserResponseDto.toDomain(user);

        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserCreateRequestDto userCreateRequestDto) {

        User user = userService.registerUser(userCreateRequestDto);

        UserResponseDto userResponseDto = UserResponseDto.toDomain(user);

        return ResponseEntity.ok(userResponseDto);
    }


    // 스프링 시큐리티 + JWT로 인증된 사용자 정보 추출해야됨(토큰)
    @PutMapping("/me/profile")
    public ResponseEntity<UserResponseDto> updateProfile(
            @PathVariable Long id,
            @RequestBody UserUpdateNicknameRequestDto userUpdateNicknameRequestDto) {

        User user = userService.updateNickname(id, userUpdateNicknameRequestDto);

        UserResponseDto userResponseDto = UserResponseDto.toDomain(user);

        return ResponseEntity.ok(userResponseDto);

    }

    // 스프링 시큐리티 + JWT로 인증된 사용자 정보 추출해야됨(토큰)
    @PutMapping("/me/password")
    public ResponseEntity<UserResponseDto> updatePassword(
            @PathVariable Long id,
            @RequestBody UserUpdatePasswordRequestDto userUpdatePasswordRequestDto) {

        User user = userService.updatePassword(id, userUpdatePasswordRequestDto);

        UserResponseDto userResponseDto = UserResponseDto.toDomain(user);

        return ResponseEntity.ok(userResponseDto);
    }

}
