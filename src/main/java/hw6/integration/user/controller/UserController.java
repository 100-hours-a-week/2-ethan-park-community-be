package hw6.integration.user.controller;

import hw6.integration.user.auth.UserPrincipal;
import hw6.integration.user.domain.User;
import hw6.integration.user.dto.*;
import hw6.integration.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
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

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {

        User user = userService.getUserById(id);

        UserResponseDto userResponseDto = UserResponseDto.toDomain(user);

        return ResponseEntity.ok(userResponseDto);
    }

    // ✅ 현재 로그인한 사용자 정보 조회
    @GetMapping("/users/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userService.getUserById(userPrincipal.getId());
        return ResponseEntity.ok(UserResponseDto.toDomain(user));
    }

    @PostMapping(value = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> registerUser(
            @ModelAttribute UserSignupRequestDto userSignupRequestDto) {

        System.out.println(userSignupRequestDto.getProfileImage());
        User user = userService.registerUser(userSignupRequestDto);



        //UserResponseDto userResponseDto = UserResponseDto.toDomain(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponseDto> login(@ModelAttribute UserLoginRequestDto userLoginRequestDto) {
        String token = userService.login(userLoginRequestDto);
        return ResponseEntity.ok(new TokenResponseDto(token));
    }



    @PutMapping("/me/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> updateProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal, // 인증된 사용자 정보
            @RequestBody UserUpdateNicknameRequestDto userUpdateNicknameRequestDto) {
        User user = userService.updateNickname(userPrincipal.getId(), userUpdateNicknameRequestDto);

        UserResponseDto userResponseDto = UserResponseDto.toDomain(user);

        return ResponseEntity.ok(userResponseDto);

    }


    @PutMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> updatePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody UserUpdatePasswordRequestDto userUpdatePasswordRequestDto) {

        userService.updatePassword(userPrincipal.getId(), userUpdatePasswordRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        userService.deleteUser(userPrincipal.getId());
        return ResponseEntity.noContent().build();


    }

}
