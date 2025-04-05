package hw6.integration.user.controller;

import hw6.integration.user.auth.UserPrincipal;
import hw6.integration.user.domain.User;
import hw6.integration.user.dto.*;
import hw6.integration.user.service.UserAuthService;
import hw6.integration.user.service.UserReadService;
import hw6.integration.user.service.UserWriterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserWriterService userWriterService;
    private final UserAuthService userAuthService;
    private final UserReadService userReadService;

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {

        User user = userReadService.getUserById(id);

        UserResponseDto userResponseDto = UserResponseDto.fromDomain(user);

        return ResponseEntity.ok(userResponseDto);
    }

    // ✅ 현재 로그인한 사용자 정보 조회
    @GetMapping("/users/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userReadService.getUserById(userPrincipal.getId());
        return ResponseEntity.ok(UserResponseDto.fromDomain(user));
    }

    @PostMapping(value = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> registerUser(
            @Valid @ModelAttribute UserSignupRequestDto userSignupRequestDto) {

        userWriterService.registerUser(userSignupRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @ModelAttribute UserLoginRequestDto userLoginRequestDto) {
        String token = userAuthService.login(userLoginRequestDto);
        return ResponseEntity.ok(TokenResponseDto.from(token));
    }

    @PutMapping("/me/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserNicknameUpdateResponseDto> updateProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal, // 인증된 사용자 정보
            @Valid @RequestBody UserUpdateNicknameRequestDto userUpdateNicknameRequestDto) {

        return ResponseEntity.ok(UserNicknameUpdateResponseDto.fromDomain(userWriterService.updateNickname(userPrincipal.getId(), userUpdateNicknameRequestDto)));
    }


    @PutMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> updatePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UserUpdatePasswordRequestDto userUpdatePasswordRequestDto) {

        userWriterService.updatePassword(userPrincipal.getId(), userUpdatePasswordRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        userWriterService.deleteUser(userPrincipal.getId());
        return ResponseEntity.ok().build();

    }

}
