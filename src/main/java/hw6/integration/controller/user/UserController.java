package hw6.integration.controller.user;

import hw6.integration.domain.model.User;
import hw6.integration.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.getUserByAll();

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // 상태 코드 204
        }

        List<UserDto> userDtos = users.stream()
                .map(UserDto::fromUser)
                .toList();

        return ResponseEntity.ok(userDtos); // 상태 코드 200
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {

        User user = userService.getUserById(id);

        UserDto userDto = UserDto.fromUser(user);

        return ResponseEntity.ok(userDto);
    }

}
