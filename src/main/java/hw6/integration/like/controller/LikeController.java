package hw6.integration.like.controller;

import hw6.integration.like.dto.LikeStatusResponseDto;
import hw6.integration.like.service.LikeService;
import hw6.integration.post.service.PostService;
import hw6.integration.user.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/likes")
public class LikeController {

    private final LikeService likeService;
    private final PostService postService;

    @PostMapping
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<Void> likePost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long postId) {

        Long userId = userPrincipal.getId();

        likeService.toggleLike(userId, postId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status")
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<LikeStatusResponseDto> getLikeStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long postId) {

        boolean isLiked = likeService.isLiked(userPrincipal.getId(), postId);
        int likeCount = postService.getPostById(postId).getLike_count();

        return ResponseEntity.ok(new LikeStatusResponseDto(isLiked, likeCount));
    }


}
